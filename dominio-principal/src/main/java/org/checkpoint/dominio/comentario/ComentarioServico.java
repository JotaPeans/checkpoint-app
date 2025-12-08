package org.checkpoint.dominio.comentario;

import org.checkpoint.dominio.jogo.AvaliacaoId;
import org.checkpoint.dominio.lista.ListaId;
import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;

import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class ComentarioServico {
    private final ComentarioRepositorio comentarioRepositorio;

    public ComentarioServico(ComentarioRepositorio comentarioRepositorio) {
        notNull(comentarioRepositorio, "O repositório de comentarios não pode ser nulo");
        this.comentarioRepositorio = comentarioRepositorio;
    }

    // =====================
    // Criação de comentários
    // =====================
    public void addComentarioAvaliacaoRaiz(User autor, AvaliacaoId avaliacaoAlvoId, String conteudo) {
        notNull(autor, "O autor não pode ser nulo");
        notNull(avaliacaoAlvoId, "O id da avaliação alvo não pode ser nulo");
        notNull(conteudo, "O conteúdo do comentário não pode ser nulo");

        validarTamanhoConteudo(conteudo);

        this.comentarioRepositorio.createComentario(autor.getUserId(), conteudo, avaliacaoAlvoId, null, null);
    }

    public void addComentarioListaRaiz(User autor, ListaId listaAlvoId, String conteudo) {
        notNull(autor, "O autor não pode ser nulo");
        notNull(listaAlvoId, "O id da lista alvo não pode ser nulo");
        notNull(conteudo, "O conteúdo do comentário não pode ser nulo");

        validarTamanhoConteudo(conteudo);

        this.comentarioRepositorio.createComentario(autor.getUserId(), conteudo, null, listaAlvoId, null);
    }

    public void replyComentario(User autor, ComentarioId comentarioPaiId, String conteudo) {
        notNull(autor, "O autor não pode ser nulo");
        notNull(comentarioPaiId, "O id do comentário pai não pode ser nulo");
        notNull(conteudo, "O conteúdo da resposta não pode ser nulo");

        validarTamanhoConteudo(conteudo);

        this.comentarioRepositorio.createComentario(autor.getUserId(), conteudo, null, null, comentarioPaiId);
    }

    // =====================
    // Validação de conteúdo
    // =====================
    private void validarTamanhoConteudo(String conteudo) {
        if (conteudo.length() < 5) {
            throw new IllegalArgumentException("O comentário deve ter no mínimo 5 caracteres");
        }
        if (conteudo.length() > 500) {
            throw new IllegalArgumentException("O comentário deve ter no máximo 500 caracteres");
        }
    }

    // =====================
    // Listagem
    // =====================
    public List<Comentario> listComentariosByAvaliacaoAlvo(AvaliacaoId avaliacaoAlvoId) {
        notNull(avaliacaoAlvoId, "O id da avaliação alvo não pode ser nulo");
        return this.comentarioRepositorio.listComentariosRaizByAvaliacaoAlvo(avaliacaoAlvoId);
    }

    public List<Comentario> listComentariosByListaAlvo(ListaId listaAlvoId) {
        notNull(listaAlvoId, "O id da lista alvo não pode ser nulo");
        return this.comentarioRepositorio.listComentariosRaizByListaAlvo(listaAlvoId);
    }

    public List<Comentario> listComentariosByPai(ComentarioId comentarioPaiId) {
        notNull(comentarioPaiId, "O id do comentário pai não pode ser nulo");
        return this.comentarioRepositorio.listComentariosByPai(comentarioPaiId);
    }

    // =====================
    // Edição e exclusão
    // =====================
    public void editComentario(User autor, ComentarioId comentarioId, String novoConteudo) {
        notNull(autor, "O autor não pode ser nulo");
        notNull(comentarioId, "O id do comentário não pode ser nulo");
        notNull(novoConteudo, "O novo conteúdo não pode ser nulo");

        validarTamanhoConteudo(novoConteudo);

        Comentario comentario = this.comentarioRepositorio.getComentarioById(comentarioId);
        notNull(comentario, "Comentário não encontrado");

        isTrue(comentario.getAutorId().equals(autor.getUserId()), "Comentário não encontrado");

        comentario.setConteudo(novoConteudo);

        this.comentarioRepositorio.saveComentario(comentario);
    }

    // =====================
    // Curtidas
    // =====================
    public void toggleComentarioLike(User user, ComentarioId comentarioId) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(comentarioId, "O id do comentário não pode ser nulo");

        Comentario comentario = this.comentarioRepositorio.getComentarioById(comentarioId);
        notNull(comentario, "Comentário não encontrada");

        List<UserId> curtidas = comentario.getCurtidas();
        if (curtidas.contains(user.getUserId())) {
            curtidas.remove(user.getUserId());
        } else {
            curtidas.add(user.getUserId());
        }

        comentario.setCurtidas(curtidas);
        this.comentarioRepositorio.saveComentario(comentario);
    }

    public void deleteComentario(User autor, ComentarioId comentarioId) {
        notNull(autor, "O autor não pode ser nulo");
        notNull(comentarioId, "O id do comentário não pode ser nulo");

        Comentario comentario = this.comentarioRepositorio.getComentarioById(comentarioId);

        isTrue(comentario.getAutorId().equals(autor.getUserId()), "Comentario não encontrado");

        this.deleteComentarioRecursivo(comentarioId);
    }

    private void deleteComentarioRecursivo(ComentarioId comentarioId) {
        List<Comentario> filhos = this.comentarioRepositorio.listComentariosByPai(comentarioId);
        for (Comentario filho : filhos) {
            deleteComentarioRecursivo(filho.getId());
        }
        this.comentarioRepositorio.deleteComentario(comentarioId);
    }
}
