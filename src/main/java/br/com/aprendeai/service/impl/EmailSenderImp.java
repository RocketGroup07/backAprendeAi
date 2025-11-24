package br.com.aprendeai.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.aprendeai.service.EmailSender;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderImp implements EmailSender {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailSenderImp(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    
    
	@Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("suporteaprendeai@gmail.com");

        mailSender.send(message);
    }
	
	@Override
	public void sendHtmlEmail(String to, String subject, String templateName, Context context) {
        String htmlContent = templateEngine.process(templateName, context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("suporteaprendeai@gmail.com");

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email HTML", e);
        }
    }
}
