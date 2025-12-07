package org.checkpoint.dominio.jogo;

import org.checkpoint.dominio.user.UserId;

import java.util.List;

public interface JogoRepositorio {
    void saveJogo(Jogo jogo);

    Jogo getJogo(JogoId id);

    List<Jogo> listJogos();

    Avaliacao getAvaliacaoById(AvaliacaoId id);

    void saveAvaliacao(Avaliacao avaliacao);

    Avaliacao createAvaliacao(UserId autorId, JogoId jogoId, Double nota, String comentario);

    List<Avaliacao> getAvaliacoesByJogoId(JogoId jogoId);


    Tag createTag(Jogo jogo, String nome);

    Tag getTagByName(String nome);

    Tag getTagById(TagId tagId);

    void saveTag(Jogo jogo, Tag tag);

    List<RequisitosDeSistema> getRequisitosDeSistemaByJogoId(JogoId jogoId);
}
