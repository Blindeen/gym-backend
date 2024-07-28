package project.gym.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    private void sendMail(
            String to,
            String subject,
            String templateName,
            Map<String, Object> vars
    ) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        email.setFrom(username);
        email.setTo(to);
        email.setSubject(subject);

        Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariables(vars);

        String htmlContent = htmlTemplateEngine.process(templateName, ctx);
        email.setText(htmlContent, true);

        emailSender.send(mimeMessage);
    }

    @Async
    public void sendSignUpConfirmation(String emailTo, String firstName) {
        String subject = "Sign-up confirmation";
        String templateName = "signup";
        Map<String, Object> vars = Map.of("email", emailTo, "firstName", firstName);

        try {
            sendMail(emailTo, subject, templateName, vars);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
