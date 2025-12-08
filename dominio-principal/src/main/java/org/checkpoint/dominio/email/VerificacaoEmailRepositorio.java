package org.checkpoint.dominio.email;

import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;

public interface VerificacaoEmailRepositorio {
    void save(VerificacaoEmail verificacaoEmail);

    VerificacaoEmail getByToken(Token token);

    void deleteToken(Token token);

    VerificacaoEmail createToken(Token token, UserId id);

    VerificacaoEmail getByUser(User user);
}
