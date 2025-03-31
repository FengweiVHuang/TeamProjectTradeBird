package com.traderbird.controller

import com.traderbird.implementation.scheduled_task.ScheduledTaskManager
import com.traderbird.implementation.scheduled_task.verification.RegisterUserVerificationTask
import com.traderbird.implementation.scheduled_task.verification.ResetPasswordTask
import com.traderbird.model.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import jakarta.servlet.http.Cookie

import com.traderbird.security.JwtTokenProvider
import com.traderbird.service.EmailService
import com.traderbird.service.FileService
import com.traderbird.service.ImageService
import com.traderbird.service.UserService
import com.traderbird.util.*
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.*


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
    private val emailService: EmailService,
    private val fileService: FileService,
    private val imageService: ImageService,
) {
    private val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    data class RegisterStage1RequestData(
        val username: String, val email: String
    )

    @PostMapping("/register-stage-1")
    fun registerUserStage1(
        @RequestBody requestData: RegisterStage1RequestData
    ): ResponseEntity<Any> {

        val (username, email) = requestData
        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("User already exists")
        }

        if ("\\w+@\\w+\\.ubc\\.ca".toRegex().notMatches(email)) {
            return ResponseEntity.badRequest().body("Not UBC email")
        }

        val code = (100000..999999).random()

        ScheduledTaskManager["register_verification_$username"] =
            object : RegisterUserVerificationTask(username, email, code) {
                override fun onRemove(): Boolean {
                    return ScheduledTaskManager.removeTask("register_verification_${this.username}")
                }
            }

        emailService.sendEmail(
            email, "TraderBird Registration Verification Code", "Your verification code is: $code"
        )
        return ResponseEntity.ok().build()
    }

    data class RegisterStage2RequestData(
        val username: String, val verificationCode: String, val password: String
    )

    @PostMapping("/register-stage-2")
    fun registerUserStage2(
        @RequestBody requestData: RegisterStage2RequestData
    ): ResponseEntity<Any> {
        val (username, verificationCode, password) = requestData
        val task = ScheduledTaskManager["register_verification_$username"]

        if (task is RegisterUserVerificationTask) {
            val result = task.handle(verificationCode.toInt(), password)
            if (!result) {
                return ResponseEntity.badRequest().body("Verification code is incorrect")
            }
            task.onRemove()

            return ResponseEntity.ok().build()

        }

        return ResponseEntity.badRequest().body(
            "User not found, verification code expired," + " or verification code is not requested when registering"
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody userLoginRequestData: UserLoginRequestData, response: HttpServletResponse
    ): ResponseEntity<Any> {

        logger.info("User login attempt: ${userLoginRequestData.username}")

        // check if user exists
        if (!userService.existsByUsername(userLoginRequestData.username)) {
            logger.error("User not found: ${userLoginRequestData.username}")
            return ResponseEntity.badRequest().body("User not found")
        }

        // check if password is correct
        val user = userService.getUserByUsername(userLoginRequestData.username)
        if (user == null || !userService.checkPassword(user, userLoginRequestData.password)) {
            logger.error("Password is incorrect for user: ${userLoginRequestData.username}")
            return ResponseEntity.badRequest().body("Password is incorrect")
        }

        // create authentication token, this should never fail
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                userLoginRequestData.username, userLoginRequestData.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt: String = jwtTokenProvider.generateToken(authentication)
        logger.info("User logged in successfully: ${userLoginRequestData.username}")

        // create http only cookie
        val cookie = Cookie("token", jwt)
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.path = "/"
        cookie.maxAge = 7 * 24 * 60 * 60
        cookie.setAttribute("SameSite", "None")

        // add cookie to response
        response.addCookie(cookie)

        return ResponseEntity.ok("Login successful")
    }

    @GetMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("token", "")
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.path = "/"
        cookie.maxAge = 0
        cookie.setAttribute("SameSite", "None")

        response.addCookie(cookie)

        return ResponseEntity.ok("Logout successful")
    }

    @GetMapping("/get_details")
    fun getDetails(): ResponseEntity<Any> {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name
        val user = userService.getUserByUsername(username)
        return if (user != null) {
            ResponseEntity.ok(user.asResponseObject())
        } else {
            ResponseEntity.badRequest().body("User not found")
        }
    }

    @DeleteMapping("/delete")
    fun deleteUser(): ResponseEntity<Any> {
        val user = UserUtils.getNowAuthenticatedUser()
        user?.let {
            GetSpringBean(UserService::class.java).deleteUser(it)
            return ResponseEntity.ok("User deleted")
        }
        return ResponseEntity.badRequest().body("User not found")
    }


    @PostMapping("/upload_profile_picture", consumes = ["multipart/form-data"])
    fun uploadProfilePicture(
        @RequestParam file: MultipartFile,
        @RequestParam name: String
    ): StandardSimpleResponse {

        val fileName = UserUtils.getNowAuthenticatedUser()?.id?.let {
            "$it"
        } ?: return StandardSimpleResponseBadRequest

        val success = fileService.convertMultipartFileToJpegAndSave("profile_pictures", fileName, file)

        return if (success) {
            StandardSimpleResponseOk
        } else {
            StandardSimpleResponseBadRequest
        }
    }

    data class UserLoginRequestData(
        val username: String, val password: String
    )

    @GetMapping("/get_profile_picture/id={id}")
    fun getProfilePicture(@PathVariable id: Long): ResponseEntity<Any> {
        val fileName = "$id.jpeg"
        val filePath: Path = Paths.get("file_storage/profile_pictures").resolve(fileName).normalize()

        return responseToImage(filePath, fileName)
    }

    @GetMapping("get_username/id={id}")
    fun getUsername(@PathVariable id: Long): StandardResponse {
        if (id == 0L) {
            return UserUtils.getNowAuthenticatedUser()?.let {
                StandardResponseEntityOk("username" to it.username)
            } ?: StandardResponseEntityForbidden("reason" to "Not logged in")
        }
        return userService.getUserById(id)?.let {
            StandardResponseEntityOk("username" to it.username)
        } ?: StandardResponseEntityBadRequest("reason" to "User not found")
    }

    @GetMapping("/get_logged_in_user_id")
    fun getLoggedInUserId(): StandardResponse {
        val user = UserUtils.getNowAuthenticatedUser()
        return if (user != null) {
            StandardResponseEntityOk("id" to user.id)
        } else {
            StandardResponseEntityForbidden("reason" to "Not logged in")
        }
    }

    @GetMapping("/reset-password-stage-1/username={username}")
    fun resetPasswordStage1(@PathVariable username: String): StandardSimpleResponse {
        val user = userService.getUserByUsername(username)
        if (user == null) {
            return StandardSimpleResponseBadRequest
        }

        val code = (100000..999999).random()
        val email = user.profile.email
        ScheduledTaskManager["reset_password_$username"] = object : ResetPasswordTask(username, code) {
            override fun onRemove(): Boolean {
                return ScheduledTaskManager.removeTask("reset_password_$username")
            }
        }

        emailService.sendEmail(email, "TraderBird Reset Password Verification Code", "Your verification code is: $code")
        return StandardSimpleResponseOk
    }

    data class ResetPasswordStage2RequestData(
        val username: String, val verificationCode: String, val password: String
    )

    @PostMapping("/reset-password-stage-2")
    fun resetPasswordStage2(@RequestBody requestData: ResetPasswordStage2RequestData): StandardSimpleResponse {
        val (username, verificationCode, password) = requestData
        val task = ScheduledTaskManager["reset_password_$username"]

        if (task is ResetPasswordTask) {
            val result = task.handle(verificationCode.toInt(), password)
            if (!result) {
                return StandardSimpleResponseUnauthorized
            }
            task.onRemove()
            return StandardSimpleResponseOk
        }

        return StandardSimpleResponseBadRequest
    }
}

