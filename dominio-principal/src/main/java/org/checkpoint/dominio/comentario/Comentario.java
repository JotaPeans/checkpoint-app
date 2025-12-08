package org.checkpoint.dominio.comentario;

import org.checkpoint.dominio.jogo.AvaliacaoId;
import org.checkpoint.dominio.lista.ListaId;
import org.checkpoint.dominio.user.UserId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class Comentario {
    private final ComentarioId id;
    private UserId autorId;
    private String conteudo;
    private Date data;
    private List<UserId> curtidas;

    private AvaliacaoId avaliacaoAlvoId;
    private ListaId listaAlvoId;

    private ComentarioId comentarioPaiId;

    public Comentario(
            UserId autorId,
            String conteudo,
            AvaliacaoId avaliacaoAlvoId,
            ListaId listaAlvoId,
            ComentarioId comentarioPaiId
    ) {
        notNull(autorId, "O id do autor não pode ser nulo");
        notNull(conteudo, "O conteúdo do comentário não pode ser nulo");

        this.id = null;

        setAutorId(autorId);
        setConteudo(conteudo);
        setData(new Date());
        setAvaliacaoAlvoId(avaliacaoAlvoId);
        setListaAlvoId(listaAlvoId);
        setComentarioPaiId(comentarioPaiId);
        setCurtidas(new ArrayList<>());
    }

    public Comentario(
            ComentarioId id,
            UserId autorId,
            String conteudo,
            Date data,
            AvaliacaoId avaliacaoAlvoId,
            ListaId listaAlvoId,
            ComentarioId comentarioPaiId,
            List<UserId> curtidas
    ) {
        notNull(id, "O id não pode ser nulo");
        notNull(autorId, "O id do autor não pode ser nulo");
        notNull(conteudo, "O conteúdo do comentário não pode ser nulo");
        notNull(data, "A data do comentário não pode ser nulaa");
        notNull(curtidas, "A lista de curtidas não pode ser nula");

        this.id = id;

        setAutorId(autorId);
        setConteudo(conteudo);
        setData(data);
        setAvaliacaoAlvoId(avaliacaoAlvoId);
        setListaAlvoId(listaAlvoId);
        setComentarioPaiId(comentarioPaiId);
        setCurtidas(curtidas);
    }

    public ComentarioId getId() {
        return id;
    }

    public UserId getAutorId() {
        return autorId;
    }

    public void setAutorId(UserId autorId) {
        this.autorId = autorId;
    }

    public String getConteudo() {
        return this.conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public AvaliacaoId getAvaliacaoAlvoId() {
        return avaliacaoAlvoId;
    }

    public void setAvaliacaoAlvoId(AvaliacaoId avaliacaoAlvo) {
        this.avaliacaoAlvoId = avaliacaoAlvo;
    }

    public ListaId getListaAlvoId() {
        return listaAlvoId;
    }

    public void setListaAlvoId(ListaId listaAlvo) {
        this.listaAlvoId = listaAlvo;
    }

    public ComentarioId getComentarioPaiId() {
        return comentarioPaiId;
    }

    public void setComentarioPaiId(ComentarioId comentarioPaiId) {
        this.comentarioPaiId = comentarioPaiId;
    }

    public List<UserId> getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(List<UserId> curtidas) {
        this.curtidas = curtidas;
    }

    @Override
    public String toString() {
        return this.conteudo;
    }
}
