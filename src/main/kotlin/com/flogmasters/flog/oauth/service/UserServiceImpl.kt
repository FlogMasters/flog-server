package com.flogmasters.flog.oauth.service

import javax.mail.Authenticator
import org.springframework.stereotype.Service
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
class UserServiceImpl() : UserService {

    override fun sendEmailForFoundId(email: String): Boolean {
        if(validateEmail(email)){
            return sendEmail(email)
        }
        return false
    }

    private fun validateEmail(email: String): Boolean {
        val regex = "^[_a-zA-Z0-9-.]+@[.a-zA-Z0-9-]+.[a-zA-Z]+$"
        return email.matches(Regex(regex))
    }

    private fun sendEmail(email: String):Boolean {
        val userName = "test"
        val password = "test"
        val props = Properties()
        props.put("mail.smtp.host","smtp.sendgrid.net")
        props.put("mail.smtp.port","465")
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.sendgrid.net")

        try {
            val auth = SMTPAuthenticator()
            val mailSession = Session.getDefaultInstance(props, auth)
            mailSession.debug = true
            val mimeMessage = MimeMessage(mailSession)
            mimeMessage.setFrom(InternetAddress("*"))
            mimeMessage.setRecipient(Message.RecipientType.TO, InternetAddress(email))
            mimeMessage.subject = "메일 테스트"
            mimeMessage.setText("메일 테스트")
            Transport.send(mimeMessage)
            return true
        } catch (e: Exception) {
            throw Exception(e)
        }

    }

    class SMTPAuthenticator : Authenticator() {

        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication("apikey", "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")
        }
    }

}