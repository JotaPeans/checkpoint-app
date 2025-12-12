package org.checkpoint.dominio.user.observer;

import org.checkpoint.dominio.email.EmailSenderService;
import org.checkpoint.dominio.user.User;

import static org.apache.commons.lang3.Validate.notNull;

public class EmailFollowObserver implements FollowEventObserver {
    private final EmailSenderService emailSenderService;

    public EmailFollowObserver(EmailSenderService emailSenderService) {
        notNull(emailSenderService, "O serviço de email não pode ser nulo");
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void onSolicitacaoParaSeguir(User solicitante, User alvo) {
        String assunto = "Solicitação para seguir";
        String mensagem = String.format(
                "O usuário <b>%s</b> gostaria de seguir você.",
                solicitante.getNome()
        );

        emailSenderService.sendEmail(alvo.getEmail(), assunto, mensagem);
    }
}