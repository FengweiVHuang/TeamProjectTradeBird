package com.traderbird.service

import org.slf4j.LoggerFactory
import org.springframework.mail.MailAuthenticationException
import org.springframework.mail.MailSendException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

enum class EmailStatus {
    SUCCESS,
    SEND_ERROR,
    AUTH_ERROR,
    UNKNOWN_ERROR
}

@Service
class EmailService(private val mailSender: JavaMailSender) {

    private val logger = LoggerFactory.getLogger(EmailService::class.java)

    fun sendEmail(to: String, subject: String, text: String) : EmailStatus {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = subject
        message.text = text

        try {
            mailSender.send(message)
            logger.info("Email sent successfully to $to with subject: $subject")
            return EmailStatus.SUCCESS
        } catch (e: MailSendException) {
            logger.error("Failed to send email: ${e.message}")
            return EmailStatus.SEND_ERROR
        } catch (e: MailAuthenticationException) {
            logger.error("Authentication failed: ${e.message}")
            return EmailStatus.AUTH_ERROR
        } catch (e: Exception) {
            logger.error("Unknown error occurred: ${e.message}")
            return EmailStatus.UNKNOWN_ERROR
        }
    }
}