package com.gestioneventos.infrastructure;

public interface EmailAdapter {
    // Envia un correo electronico
    // to: destinatario
    // subject: asunto
    // body: cuerpo del mensaje
    void sendEmail(String to, String subject, String body);
}
