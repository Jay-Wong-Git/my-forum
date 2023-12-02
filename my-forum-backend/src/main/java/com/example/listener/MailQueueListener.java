package com.example.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        String email = (String) data.get("email");
        String code = (String)data.get("code");
        String type = (String) data.get("type");
        SimpleMailMessage message = switch (type) {
            case ("register") -> createMessage("Register validate code",
                    "This is your validate code for register: " + code,
                    email);
            case ("reset") -> createMessage("Reset password validate code",
                    "This is your validate code for reset password: " + code,
                    email);
            case ("modify") -> createMessage("Modify email validate code",
                    "This is your validate code for modify email: " + code,
                    email);
            default -> null;
        };
        if (message == null) {
            return;
        }
        javaMailSender.send(message);
    }

    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
