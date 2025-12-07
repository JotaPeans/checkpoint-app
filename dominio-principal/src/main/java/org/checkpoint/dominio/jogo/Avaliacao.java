package org.checkpoint.dominio.jogo;

import org.checkpoint.dominio.user.UserId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class Avaliacao {
    private final AvaliacaoId id;

    private UserId autorId;
    private JogoId jogoId;
    private Double nota;
    private String comentario;
    private Date data;
    private List<UserId> curtidas;

    public Avaliacao(
            UserId autorId,
            JogoId jogoId,
            Double nota,
            String comentario
    ) {
        notNull(autorId, "O id do autor não pode ser nulo");
        notNull(jogoId, "O id do jogo não pode ser nulo");
        notNull(nota, "A nota pode ser nula");

        this.id = null;

        setAutorId(autorId);
        setJogoId(jogoId);
        setNota(nota);
        setComentario(comentario);
        setData(new Date());
        setCurtidas(new ArrayList<>());
    }

    public Avaliacao(
            AvaliacaoId id,
            UserId autorId,
            JogoId jogoId,
            Double nota,
            String comentario,
            Date data,
            List<UserId> curtidas
    ) {
        notNull(id, "O id não pode ser nulo");
        notNull(autorId, "O id do autor não pode ser nulo");
        notNull(jogoId, "O id do jogo não pode ser nulo");
        notNull(nota, "A nota pode ser nula");
        notNull(data, "A data da avaliação não pode ser nulo");
        notNull(curtidas, "A lista de curtidas da avaliação não pode ser nulo");

        this.id = id;

        setAutorId(autorId);
        setJogoId(jogoId);
        setNota(nota);
        setComentario(comentario);
        setData(data);
        setCurtidas(curtidas);
    }

    public AvaliacaoId getId() {
        return id;
    }

    public UserId getAutorId() {
        return autorId;
    }

    public void setAutorId(UserId autorId) {
        this.autorId = autorId;
    }

    public JogoId getJogoId() {
        return jogoId;
    }

    public void setJogoId(JogoId jogoId) {
        this.jogoId = jogoId;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<UserId> getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(List<UserId> curtidas) {
        this.curtidas = curtidas;
    }

    @Override
    public String toString() {
        return this.comentario;
    }
}
