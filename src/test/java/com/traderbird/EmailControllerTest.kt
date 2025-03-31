package com.traderbird

import com.traderbird.service.EmailService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.Mockito.verify
import org.mockito.kotlin.times

// This doesn't work for some reason
/*
@SpringBootTest
@AutoConfigureMockMvc
class EmailControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var emailService: EmailService

    @Test
    fun `should send email and return success message`() {
        val to =

        mockMvc.get("/send-email") {
            param("to", to)
        }
            .andExpect {
                status { isOk() }
                content { string("Email sent to $to") }
            }

        verify(emailService, times(1)).sendEmail(
            to = to,
            subject = "Test Email",
            text = "This is a test email sent from Spring Boot."
        )
    }
}
*/
