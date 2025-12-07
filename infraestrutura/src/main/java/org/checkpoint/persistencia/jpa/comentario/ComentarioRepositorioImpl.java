package org.checkpoint.persistencia.jpa.comentario;

import org.checkpoint.dominio.comentario.Comentario;
import org.checkpoint.dominio.comentario.ComentarioId;
import org.checkpoint.dominio.comentario.ComentarioRepositorio;
import org.checkpoint.dominio.jogo.AvaliacaoId;
import org.checkpoint.dominio.lista.ListaId;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.persistencia.jpa.JpaMapeador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ComentarioRepositorioImpl implements ComentarioRepositorio {
    private @Autowired ComentarioJpaRepositorio repositorio;

    private @Autowired JpaMapeador mapeador;

    @Override
    public void saveComentario(Comentario comentario) {
        repositorio.save(mapeador.map(comentario, ComentarioJpa.class));
    }

    @Override
    public void createComentario(UserId autorId, String conteudo, AvaliacaoId avaliacaoAlvoId, ListaId listaAlvoId, ComentarioId comentarioPaiId) {
        Comentario comentario = new Comentario(autorId, conteudo, avaliacaoAlvoId, listaAlvoId, comentarioPaiId);

        repositorio.save(mapeador.map(comentario, ComentarioJpa.class));
    }

    @Override
    public Comentario getComentarioById(ComentarioId id) {
        return mapeador.map(repositorio.findById(id.getId()), Comentario.class);
    }

    @Override
    public List<Comentario> listComentariosByPai(ComentarioId id) {
        List<ComentarioJpa> comentariosJpa = repositorio.findByComentarioPaiId(id.getId());

        List<Comentario> comentarios = new ArrayList<>();

        for(ComentarioJpa comentario : comentariosJpa) {
            comentarios.add(mapeador.map(comentario, Comentario.class));
        }

        return comentarios;
    }

    @Override
    public List<Comentario> listComentariosRaizByAvaliacaoAlvo(AvaliacaoId avaliacaoAlvoId) {
        List<ComentarioJpa> comentariosJpa = repositorio.findByAvaliacaoAlvoIdAndComentarioPaiIdIsNull(avaliacaoAlvoId.getId());

        List<Comentario> comentarios = new ArrayList<>();

        for(ComentarioJpa comentario : comentariosJpa) {
            comentarios.add(mapeador.map(comentario, Comentario.class));
        }

        return comentarios;
    }

    @Override
    public List<Comentario> listComentariosRaizByListaAlvo(ListaId listaAlvoId) {
        List<ComentarioJpa> comentariosJpa = repositorio.findByListaAlvoIdAndComentarioPaiIdIsNull(listaAlvoId.getId());

        List<Comentario> comentarios = new ArrayList<>();

        for(ComentarioJpa comentario : comentariosJpa) {
            comentarios.add(mapeador.map(comentario, Comentario.class));
        }

        return comentarios;
    }

    @Override
    public void deleteComentario(ComentarioId comentarioId) {
        Comentario comentario = this.getComentarioById(comentarioId);

        repositorio.delete(mapeador.map(comentario, ComentarioJpa.class));
    }
}
