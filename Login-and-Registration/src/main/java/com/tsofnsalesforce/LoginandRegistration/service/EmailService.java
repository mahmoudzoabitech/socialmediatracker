package com.tsofnsalesforce.LoginandRegistration.service;

import com.tsofnsalesforce.LoginandRegistration.enums.EmailTemplate;
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
    private final SpringTemplateEngine springTemplateEngine;

    @Async
    public void emailSender(String to,
                            String email,
                            EmailTemplate emailTemplate,
                            String confirmationUrl,
                            String activationCode,
                            String subject
    ) throws MessagingException {
        String templateName;
        if (emailTemplate == null){
            templateName = "confirm-email";
        }else{
            templateName = emailTemplate.getName();
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        Map<String,Object>  properties = new HashMap<>();
        properties.put("email",email);
        properties.put("confirmationUrl",confirmationUrl);
        properties.put("activation_code",activationCode);

        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setFrom("doniakhamishu@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        String template = springTemplateEngine.process(templateName,context);

        mimeMessageHelper.setText(template,true);
        mailSender.send(mimeMessage);
    }
}
