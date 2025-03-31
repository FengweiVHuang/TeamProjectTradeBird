package com.traderbird.controller

import com.traderbird.service.EmailService
import com.traderbird.service.EmailStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class EmailController(private val emailService: EmailService) {

    @GetMapping("/send-email")
    fun sendTestEmail(@RequestParam to: String): ResponseEntity<Any> {
        val subject = "Welcome to Traderbird"
        val text = "This is a test email sent from ubc traderbird.\nPlease take care of yourself, and be well."
        val status = emailService.sendEmail(to, subject, text)
        return if (status == EmailStatus.SUCCESS) {
            ResponseEntity.ok("Email sent to $to")
        } else {
            ResponseEntity.badRequest().body("Failed to send email to $to, Reason: $status")
        }
    }

}