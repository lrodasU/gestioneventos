package com.gestioneventos.infrastructure;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

// Adapter que envia correos via SMTP usando Jakarta Mail
public class SmtpEmailAdapter implements EmailAdapter {

    private final Session session;
    private final String fromAddress;

    public SmtpEmailAdapter(String host,
            int port,
            String username,
            String password,
            boolean useTls,
            String fromAddress) {
        this.fromAddress = fromAddress;

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        if (useTls) {
            props.put("mail.smtp.starttls.enable", "true");
        }

        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromAddress));
            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando email a " + to, e);
        }
    }
}
