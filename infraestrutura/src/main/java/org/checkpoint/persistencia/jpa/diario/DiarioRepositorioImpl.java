package org.checkpoint.persistencia.jpa.diario;

import org.checkpoint.dominio.diario.*;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.User;
import org.checkpoint.persistencia.jpa.JpaMapeador;
import org.checkpoint.persistencia.jpa.user.UserJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class DiarioRepositorioImpl implements DiarioRepositorio {

    @Autowired
    DiarioJpaRepositorio repositorio;

    @Autowired
    RegistroJpaRepositorio registroRepositorio;

    @Autowired
    ConquistaJpaRepositorio conquistaRepositorio;

    @Autowired
    JpaMapeador mapeador;

    @Override
    public Diario saveDiario(Diario diario) {
        var diarioJpa = mapeador.map(diario, DiarioJpa.class);

        if (diarioJpa == null) {
            return null;
        }

        return mapeador.map(repositorio.save(diarioJpa), Diario.class);
    }

    @Override
    public Diario getDiario(DiarioId id) {
        var diarioJpa = repositorio.findById(id.getId()).get();

        return mapeador.map(diarioJpa, Diario.class);
    }

    @Override
    public Diario getDiarioByDono(User user) {
        var diarioJpa = repositorio.findDiarioJpaByDono(mapeador.map(user, UserJpa.class));

        if (diarioJpa == null) {
            return null;
        }

        return mapeador.map(diarioJpa, Diario.class);
    }

    @Override
    public RegistroDiario createRegistroDiario(JogoId jogo, Diario diario, Date dataInicio, Date dataTermino) {
        var registroDiario = new RegistroDiario(jogo, diario, dataInicio, dataTermino);
        var registroDiarioJpa = mapeador.map(registroDiario, RegistroJpa.class);

        if (registroDiarioJpa == null) {
            return null;
        }

        registroDiarioJpa.diario = mapeador.map(diario, DiarioJpa.class);

        return mapeador.map(registroRepositorio.save(registroDiarioJpa), RegistroDiario.class);
    }

    @Override
    public List<RegistroDiario> listRegistros(DiarioId diarioId) {
        List<RegistroJpa> registrosJpa = this.registroRepositorio.findByDiarioId(diarioId.getId());

        List<RegistroDiario> registros = new ArrayList<>();

        for (RegistroJpa r : registrosJpa) {
            registros.add(mapeador.map(r, RegistroDiario.class));
        }

        return registros;
    }

    @Override
    public RegistroDiario getRegistroDiario(RegistroId registroId) {
        RegistroJpa registroJpa = this.registroRepositorio.findById(registroId.getId()).get();

        RegistroDiario registroDiario = mapeador.map(registroRepositorio.findById(registroId.getId()).get(), RegistroDiario.class);

        registroDiario.setDiario(mapeador.map(registroJpa.diario, Diario.class));

        return registroDiario;
    }

    @Override
    public void saveRegistroDiario(RegistroDiario registroDiario) {
        var registroDiarioJpa = mapeador.map(registroDiario, RegistroJpa.class);

        if (registroDiarioJpa != null) {
            registroRepositorio.save(registroDiarioJpa);
        }
    }

    @Override
    public void removeRegistroDiario(RegistroId registroId) {
        RegistroJpa registroJpa = this.registroRepositorio.findById(registroId.getId()).get();

        this.registroRepositorio.delete(registroJpa);
    }

    @Override
    public Conquista createConquista(String nome, Date dataDesbloqueada, boolean isUnloked, RegistroId registroId) {
        RegistroJpa registroJpa = this.registroRepositorio.findById(registroId.getId()).get();

        RegistroDiario registroDiario = mapeador.map(registroJpa, RegistroDiario.class);

        registroDiario.setDiario(mapeador.map(registroJpa.diario, Diario.class));

        var conquista = new Conquista(nome, dataDesbloqueada, isUnloked, registroDiario);
        var conquistaJpa = mapeador.map(conquista, ConquistaJpa.class);

        if (conquistaJpa == null) {
            return null;
        }

        return mapeador.map(conquistaRepositorio.save(conquistaJpa), Conquista.class);
    }

    @Override
    public List<Conquista> listConquistas(RegistroId registroId) {
        List<ConquistaJpa> conquistasJpa = this.conquistaRepositorio.findByRegistroId(registroId.getId());

        List<Conquista> conquistas = new ArrayList<>();

        for (ConquistaJpa r : conquistasJpa) {
            conquistas.add(mapeador.map(r, Conquista.class));
        }

        return conquistas;
    }

    @Override
    public Conquista getConquista(ConquistaId conquistaId) {
        ConquistaJpa conquistaJpa = conquistaRepositorio.findById(conquistaId.getId()).get();

        Conquista conquista = mapeador.map(
                conquistaJpa,
                Conquista.class);

        RegistroJpa registroJpa = this.registroRepositorio.findById(conquistaJpa.registro.id).get();

        conquista.setRegistroDiario(mapeador.map(registroJpa, RegistroDiario.class));

        return conquista;
    }

    @Override
    public void saveConquista(Conquista conquista) {
        var conquistaJpa = mapeador.map(conquista, ConquistaJpa.class);

        if (conquistaJpa != null) {
            conquistaRepositorio.save(conquistaJpa);
        }
    }

    @Override
    public void removeConquista(ConquistaId conquistaId) {
        ConquistaJpa conquistaJpa = this.conquistaRepositorio.findById(conquistaId.getId()).get();

        this.conquistaRepositorio.delete(conquistaJpa);
    }
}
