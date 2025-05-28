package com.ra.base_spring_boot.utils;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Async
    public void sendOTPEmail(String to, String username, String email, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Mã OTP xác thực");
        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("username", username);
        context.setVariable("email", email);
        String htmlContent = templateEngine.process("send_mail", context);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
