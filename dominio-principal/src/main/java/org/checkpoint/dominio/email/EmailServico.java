package org.checkpoint.dominio.email;

import org.checkpoint.dominio.user.UserId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.lang3.Validate.notNull;

public class EmailServico implements EmailSenderService {
    private final VerificacaoEmailRepositorio verificacaoEmailRepositorio;
    private final EmailStrategy emailStrategy;

    public EmailServico(VerificacaoEmailRepositorio verificacaoEmailRepositorio, EmailStrategy emailStrategy) {
        notNull(verificacaoEmailRepositorio, "O repositório de verificacao de email não pode ser nulo");
        notNull(verificacaoEmailRepositorio, "O repositório de email não pode ser nulo");

        this.verificacaoEmailRepositorio = verificacaoEmailRepositorio;
        this.emailStrategy = emailStrategy;
    }

    public void sendEmail(String recipient, String subject, String body) {
        emailStrategy.sendEmail(recipient, subject, body);
    }

    public String generateVerificationToken(String email, UserId userId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(email.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            Token token = new Token(hexString.toString());
            this.verificacaoEmailRepositorio.createToken(token, userId);

            return token.getToken();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash: SHA-256", e);
        }
    }

    public void sendVerificationEmail(String email, String token) {
        String host = "http://localhost:8080";
        String html = String.format("<a href=\"%s\" target=\"_blank\">Verificar Email</a>", host + "/api/user/verify-email/" + token);

        this.sendEmail(email, "Token de verificacao", html);
    }

    public VerificacaoEmail getVerificacaoEmailByToken(Token token) {
        return this.verificacaoEmailRepositorio.getByToken(token);
    }

    public void deleteVerificacaoEmailByToken(Token token) {
        this.verificacaoEmailRepositorio.deleteToken(token);
    }

}
