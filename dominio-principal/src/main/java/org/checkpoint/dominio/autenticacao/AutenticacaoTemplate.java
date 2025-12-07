package org.checkpoint.dominio.autenticacao;

import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.dominio.user.UserRepositorio;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public abstract class AutenticacaoTemplate implements Autenticacao {
    protected UserRepositorio userRepositorio;

    protected AutenticacaoTemplate(UserRepositorio userRepositorio) {
        this.userRepositorio = userRepositorio;
    }

    public String login(String email, String senha) {
        User user = userRepositorio.getByEmail(email);

        notNull(user, "E-mail ou senha inválidos");

        boolean isSenhaValida = validarSenha(senha, user.getSenha());

        isTrue(isSenhaValida, "E-mail ou senha inválidos");

        isTrue(user.isEmailVerificado(), "A conta não foi verificada");

        return this.gerarJwt(user.getUserId(), user.getEmail());
    }

    @Override
    abstract public String gerarJwt(UserId userId, String email);

    @Override
    abstract public boolean validarSenha(String senha, String userSenha);

    @Override
    abstract public User verificarToken(String token);
}
