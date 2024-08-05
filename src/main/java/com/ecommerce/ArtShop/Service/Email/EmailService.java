package com.ecommerce.ArtShop.Service.Email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;


    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {

        String tempName=emailTemplateName.name();
//        if (emailTemplateName == null) {
//            tempName = "confirm-email";  //just in case
//        } else {
//            tempName = emailTemplateName.name();
//        }
        //mailSender
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        //pass param
        Map<String, Object> prop = new HashMap<>();
        prop.put("username", username);
        prop.put("confirmationUrl", confirmationUrl);
        prop.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(prop);

        helper.setFrom("contact@artshop.com");
        helper.setTo(to);
        helper.setSubject(subject);

        //process template
        String template = templateEngine.process(tempName, context);
        helper.setText(template, true);
        mailSender.send(mimeMessage);


    }

}
