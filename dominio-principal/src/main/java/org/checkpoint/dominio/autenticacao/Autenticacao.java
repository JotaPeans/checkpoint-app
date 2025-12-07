package org.checkpoint.dominio.autenticacao;

import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;

public interface Autenticacao {
    String gerarJwt(UserId userId, String email);
    String login(String email, String senha);
    boolean validarSenha(String senha, String userSenha);
    User verificarToken(String token);
}
