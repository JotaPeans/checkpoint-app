package org.checkpoint.persistencia.jpa.jogo;

import org.checkpoint.dominio.jogo.*;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.persistencia.jpa.JpaMapeador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JogoRepositorioImpl implements JogoRepositorio {
    private @Autowired JogoJpaRepositorio repositorio;
    private @Autowired AvaliacaoJpaRepositorio avaliacaoRepositorio;
    private @Autowired TagJpaRepositorio tagRepositorio;
    private @Autowired RequisitosDeSistemaJpaRepositorio requisitosDeSistemaRepositorio;

    @Autowired
    JpaMapeador mapeador;

    @Override
    public void saveJogo(Jogo jogo) {
        var jogoJpa = mapeador.map(jogo, JogoJpa.class);

        repositorio.save(jogoJpa);
    }

    @Override
    public Jogo getJogo(JogoId id) {
        return mapeador.map(repositorio.findById(id.getId()), Jogo.class);
    }

    @Override
    public List<Jogo> listJogos() {
        List<JogoJpa> jogosJpa = repositorio.findAll();

        List<Jogo> jogos = new ArrayList<>();

        for (JogoJpa jogoJpa : jogosJpa) {
            jogos.add(mapeador.map(jogoJpa, Jogo.class));
        }
        return jogos;
    }

    @Override
    public Avaliacao getAvaliacaoById(AvaliacaoId id) {
        Optional<AvaliacaoJpa> avaliacaoJpa = avaliacaoRepositorio.findById(id.getId());

        if(avaliacaoJpa.isEmpty()) {
            return null;
        }

        return mapeador.map(avaliacaoJpa, Avaliacao.class);
    }

    @Override
    public void saveAvaliacao(Avaliacao avaliacao) {
        avaliacaoRepositorio.save(mapeador.map(avaliacao, AvaliacaoJpa.class));
    }

    @Override
    public Avaliacao createAvaliacao(UserId autorId, JogoId jogoId, Double nota, String comentario) {
        Avaliacao avaliacao = new Avaliacao(autorId, jogoId, nota, comentario);
        var avaliacaoJpa = avaliacaoRepositorio.save(mapeador.map(avaliacao, AvaliacaoJpa.class));

        return mapeador.map(avaliacaoJpa, Avaliacao.class);
    }

    @Override
    public List<Avaliacao> getAvaliacoesByJogoId(JogoId jogoId) {
        List<AvaliacaoJpa> avaliacoesJpa = avaliacaoRepositorio.findByJogoId(jogoId.getId());

        List<Avaliacao> avaliacoes = new ArrayList<>();

        for (AvaliacaoJpa avaliacaoJpa : avaliacoesJpa) {
            avaliacoes.add(mapeador.map(avaliacaoJpa, Avaliacao.class));
        }
        return avaliacoes;
    }

    @Override
    public Tag createTag(Jogo jogo, String nome) {
        Tag tag = new Tag(nome, jogo);

        var tagJpa = tagRepositorio.save(mapeador.map(tag, TagJpa.class));

        tagJpa.jogo =  mapeador.map(jogo, JogoJpa.class);

        return mapeador.map(this.tagRepositorio.save(tagJpa), Tag.class);
    }

    @Override
    public Tag getTagByName(String nome) {
        TagJpa tagJpa = tagRepositorio.findByNome(nome);

        if(tagJpa == null) {
            return null;
        }

        return mapeador.map(tagJpa, Tag.class);
    }

    @Override
    public Tag getTagById(TagId tagId) {
        Optional<TagJpa> tagJpa = tagRepositorio.findById(tagId.getId());

        if(tagJpa.isEmpty()) {
            return null;
        }

        return mapeador.map(tagJpa, Tag.class);
    }

    @Override
    public void saveTag(Jogo jogo, Tag tag) {
        var tagJpa = mapeador.map(tag, TagJpa.class);

        tagJpa.jogo =  mapeador.map(jogo, JogoJpa.class);

        tagRepositorio.save(tagJpa);
    }

    @Override
    public List<RequisitosDeSistema> getRequisitosDeSistemaByJogoId(JogoId jogoId) {
        List<RequisitosDeSistemaJpa> requisitosJpa = requisitosDeSistemaRepositorio.findByJogoId(jogoId.getId());

        List<RequisitosDeSistema> requisitos = new ArrayList<>();

        for (RequisitosDeSistemaJpa requisitoJpa : requisitosJpa) {
            requisitos.add(mapeador.map(requisitoJpa, RequisitosDeSistema.class));
        }

        return requisitos;
    }
}
