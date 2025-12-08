package org.checkpoint.persistencia.jpa.autenticacao;

import org.checkpoint.dominio.autenticacao.AutenticacaoTemplate;
import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.dominio.user.UserRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.apache.commons.lang3.Validate.notNull;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AutenticacaoImpl extends AutenticacaoTemplate {
    @Value("{jwt.secret}")
    private String SECRET;

    @Autowired
    public AutenticacaoImpl(UserRepositorio userRepositorio) {
        super(userRepositorio);
    }

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public String gerarJwt(UserId userId, String email) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject(String.valueOf(userId.getId()))
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + 3600_000 * 24 * 7)) // expira em 7 dias
                .withClaim("role", "user")
                .sign(algorithm);
    }

    public User verificarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        notNull(token, "Token Inválido");

        JWTVerifier verifier = JWT.require(algorithm)
                // .withIssuer("seu-issuer") // opcional
                .build();
        DecodedJWT decoded = verifier.verify(token); // lança exceção se inválido

        User user = this.userRepositorio.getUser(new UserId(Integer.parseInt(decoded.getSubject())));

        notNull(user, "Token Inválido");

        return user;
    }

    public boolean validarSenha(String senha, String userSenha) {
        return encoder.matches(senha, userSenha);
    }
}
