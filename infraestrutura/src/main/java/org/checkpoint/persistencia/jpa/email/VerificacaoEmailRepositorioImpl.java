package org.checkpoint.persistencia.jpa.email;

import java.util.Date;

import org.checkpoint.dominio.email.Token;
import org.checkpoint.dominio.email.VerificacaoEmail;
import org.checkpoint.dominio.email.VerificacaoEmailRepositorio;
import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.persistencia.jpa.JpaMapeador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VerificacaoEmailRepositorioImpl implements VerificacaoEmailRepositorio {
    @Autowired
    VerificacaoEmailJpaRepositorio repositorio;

    @Autowired
    JpaMapeador mapeador;

    @Override
    public void save(VerificacaoEmail verificacaoEmail) {
        var verificacaoEmailJpa = mapeador.map(verificacaoEmail, VerificacaoEmailJpa.class);

        if (verificacaoEmailJpa == null) {
            return;
        }

        mapeador.map(repositorio.save(verificacaoEmailJpa), VerificacaoEmailJpa.class);
    }

    @Override
    public VerificacaoEmail getByToken(Token token) {
        String tokenString = token.getToken();

        if (tokenString == null || tokenString.isEmpty()) {
            return null;
        }

        var verificacaoEmailJpa = repositorio.findVerificacaoEmailJpaByToken(tokenString);

        return mapeador.map(verificacaoEmailJpa, VerificacaoEmail.class);
    }

    @Override
    public void deleteToken(Token token) {
        String tokenString = token.getToken();

        if (tokenString == null || tokenString.isEmpty()) {
            return;
        }

        repositorio.deleteById(tokenString);
    }

    @Override
    public VerificacaoEmail createToken(Token token, UserId id) {
        Long oneDayInMills = 86400000L;
        VerificacaoEmail verificacaoEmail = new VerificacaoEmail(
                token, id, new Date(System.currentTimeMillis() + oneDayInMills));
        this.save(verificacaoEmail);
        return verificacaoEmail;
    }

    @Override
    public VerificacaoEmail getByUser(User user) {
        var verificacaoEmailJpa = repositorio.findVerificacaoEmailJpaByUserId(user.getUserId().getId());

        return mapeador.map(verificacaoEmailJpa, VerificacaoEmail.class);
    }

}
