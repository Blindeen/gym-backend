package project.gym.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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

    private final JavaMailSender emailSender;
    private final TemplateEngine htmlTemplateEngine;
    private final MessageSource messageSource;

    public MailService(JavaMailSender emailSender, TemplateEngine htmlTemplateEngine, MessageSource messageSource) {
        this.emailSender = emailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
        this.messageSource = messageSource;
    }

    private void sendMail(
            String to,
            String subject,
            String templateName,
            Map<String, Object> vars) throws MessagingException {
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
        Map<String, Object> vars = Map.of(
                "email", emailTo,
                "firstName", firstName);
        String subject = messageSource.getMessage("email.signUpConfirmation", null,
                LocaleContextHolder.getLocale());

        try {
            sendMail(emailTo, subject, "signup", vars);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
