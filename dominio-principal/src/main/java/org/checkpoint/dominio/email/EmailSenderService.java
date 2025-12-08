package org.checkpoint.dominio.email;

import org.checkpoint.dominio.user.UserId;

public interface EmailSenderService {
    void sendEmail(String recipient, String subject, String body);

    String generateVerificationToken(String email, UserId id);

    void sendVerificationEmail(String email, String token);

    VerificacaoEmail getVerificacaoEmailByToken (Token token);

    void deleteVerificacaoEmailByToken (Token token);
}
