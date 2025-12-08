package org.checkpoint.dominio.lista;

import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.UserId;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class ListaJogos {
    private final ListaId id;
    private UserId donoId;
    private String titulo;
    private boolean isPrivate;
    private List<JogoId> jogos;
    private List<UserId> curtidas;

    public ListaJogos(
            UserId donoId,
            String titulo,
            boolean isPrivate
    ) {
        notNull(donoId, "O id do dono da lista não pode ser nulo");
        notNull(titulo, "O título não pode ser nulo");

        this.id = null;

        setDonoId(donoId);
        setTitulo(titulo);
        setIsPrivate(isPrivate);
        setJogos(new ArrayList<>());
        setCurtidas(new ArrayList<>());
    }

    public ListaJogos(
            ListaId id,
            UserId donoId,
            String titulo,
            boolean isPrivate,
            List<JogoId> jogos,
            List<UserId> curtidas
    ) {
        notNull(id, "O id não pode ser nulo");
        notNull(donoId, "O id do dono da lista não pode ser nulo");
        notNull(titulo, "O título não pode ser nulo");
        notNull(jogos, "A lista de jogos não pode ser nula");
        notNull(curtidas, "A lista de curtidas não pode ser nula");

        this.id = id;

        setDonoId(donoId);
        setTitulo(titulo);
        setIsPrivate(isPrivate);
        setJogos(jogos);
        setCurtidas(curtidas);
    }

    public ListaId getId() {
        return id;
    }

    public UserId getDonoId() {
        return donoId;
    }

    public void setDonoId(UserId donoId) {
        this.donoId = donoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public List<JogoId> getJogos() {
        return jogos;
    }

    public void setJogos(List<JogoId> jogos) {
        this.jogos = jogos;
    }

    public List<UserId> getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(List<UserId> curtidas) {
        this.curtidas = curtidas;
    }

    @Override
    public String toString() {
        return this.titulo;
    }
}
