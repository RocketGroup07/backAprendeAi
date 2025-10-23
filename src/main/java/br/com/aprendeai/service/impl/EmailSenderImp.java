package br.com.aprendeai.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.aprendeai.service.EmailSender;

@Service
public class EmailSenderImp implements EmailSender {

    // @Autowired
    private JavaMailSender mailSender;

    @Override
	public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("");

        mailSender.send(message);
    }
}
