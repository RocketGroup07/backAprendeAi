package br.com.aprendeai.service;

import org.thymeleaf.context.Context;

public interface EmailSender {
    void sendEmail(String to, String subject, String body);

	void sendHtmlEmail(String to, String subject, String templateName, Context context);
}
