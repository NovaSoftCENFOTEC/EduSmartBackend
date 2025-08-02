package com.project.demo.logic.entity.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailManager {

    private static final Logger logger = LoggerFactory.getLogger(EmailManager.class);
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            logger.info("Correo enviado a: {}", toEmail);

        } catch (Exception e) {
            logger.error("Error al enviar correo a {}: {}", toEmail, e.getMessage());
        }
    }

}
