package org.checkpoint.dominio.user.observer;

import org.checkpoint.dominio.user.User;

public interface FollowEventObserver {

    void onSolicitacaoParaSeguir(User solicitante, User alvo);
}