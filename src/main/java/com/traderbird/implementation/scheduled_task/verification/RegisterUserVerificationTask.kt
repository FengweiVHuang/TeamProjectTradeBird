package com.traderbird.implementation.scheduled_task.verification

import com.traderbird.implementation.scheduled_task.AbstractTimedTask
import com.traderbird.model.ProfileBuilder
import com.traderbird.model.Role
import com.traderbird.model.UserBuilder
import com.traderbird.service.UserService
import com.traderbird.util.GetSpringBean
import java.time.Duration

open class RegisterUserVerificationTask(
    val username: String,
    val email: String,
    val verificationCode: Int
) : AbstractTimedTask(VERIFICATION_DURATION) {

    companion object {
        private val VERIFICATION_DURATION: Duration = Duration.ofMinutes(5)
    }


    @Synchronized
    override fun onRemove(): Boolean {
        return true
    }

    @Synchronized
    fun handle(verificationCode: Int, password: String): Boolean {
        this.lock()

        if (this.shouldRemove() || verificationCode != this.verificationCode)
            return false

        if (verificationCode == this.verificationCode) {
            val builder = UserBuilder()
                .username(username)
                .password(password)
                .addRole(Role.USER)
                .addProfile(
                    ProfileBuilder()
                        .email(email)
                        .build()
                )

            GetSpringBean(UserService::class.java).saveUser(builder.build())

            this.handled = true
        }

        return true
    }
}