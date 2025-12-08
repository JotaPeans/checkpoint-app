package org.checkpoint.persistencia.jpa.user;

import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.dominio.user.UserRepositorio;
import org.checkpoint.persistencia.jpa.JpaMapeador;
import org.checkpoint.persistencia.jpa.diario.DiarioJpaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Repository
public class UserRepositorioImpl implements UserRepositorio {
    @Autowired
    UserJpaRepositorio repositorio;

    @Autowired
    DiarioJpaRepositorio diarioRepositorio;

    @Autowired
    JpaMapeador mapeador;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User saveUser(User user) {
        var userJpa = mapeador.map(user, UserJpa.class);

        if(userJpa.diario != null) {
            userJpa.diario = diarioRepositorio.findDiarioJpaByDono(userJpa);
        }

        return mapeador.map(repositorio.save(userJpa), User.class);
    }

    @Override
    public User getUser(UserId id) {
        var userJpa = repositorio.findById(id.getId());

        return mapeador.map(userJpa, User.class);
    }

    @Override
    public User getByEmail(String email) {
        var userJpa = repositorio.findUserJpaByEmail(email);
        if (userJpa == null) {
            return null;
        }
        return mapeador.map(userJpa, User.class);
    }

    @Override
    public User createUser(String email, String senha, String nome) {
        String passwordHash = encoder.encode(senha);
        User savedUser = saveUser(new User(nome, email, passwordHash));

        return saveUser(savedUser);
    }

}
