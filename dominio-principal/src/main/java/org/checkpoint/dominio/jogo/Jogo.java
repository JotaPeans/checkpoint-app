package org.checkpoint.dominio.jogo;

import org.checkpoint.dominio.user.UserId;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class Jogo {
    private final JogoId id;

    private String nome;
    private String company;
    private String capaUrl;
    private String informacaoTitulo;
    private String informacaoDescricao;
    private double nota;
    private List<String> capturas;
    private List<UserId> curtidas;
    private List<Tag> tags;
    private List<RequisitosDeSistema> requisitos;
    private List<Avaliacao> avaliacoes;

    public Jogo(
            JogoId id,
            String nome,
            String company,
            String capaUrl,
            String informacaoTitulo,
            String informacaoDescricao,
            double nota,
            List<String> capturas,
            List<UserId> curtidas,
            List<Tag> tags,
            List<RequisitosDeSistema> requisitos,
            List<Avaliacao> avaliacoes
    ) {
        notNull(id, "O id não pode ser nulo");
        notNull(nome, "O nome não pode ser nulo");
        notNull(company, "A empresa do jogo não pode ser nula");
        notNull(capaUrl, "A url da capa do jogo não pode ser nula");
        notNull(informacaoTitulo, "A informação de título não pode ser nula");
        notNull(informacaoDescricao, "A informação de descrição não pode ser nula");
        notNull(capturas, "A lista de capturas não pode ser nula");
        notNull(curtidas, "A lista de curtidas não pode ser nula");
        notNull(tags, "A lista de tags pode ser nula");
        notNull(requisitos, "A lista de requisitos não pode ser nula");
        notNull(avaliacoes, "A lista de avaliações não pode ser nulo");

        this.id = id;

        setNome(nome);
        setCompany(company);
        setCapaUrl(capaUrl);
        setInformacaoTitulo(informacaoTitulo);
        setInformacaoDescricao(informacaoDescricao);
        setNota(nota);
        setCapturas(capturas);
        setCurtidas(curtidas);
        setTags(tags);
        setRequisitos(requisitos);
        setAvaliacoes(avaliacoes);
    }

    public JogoId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCapaUrl() {
        return capaUrl;
    }

    public void setCapaUrl(String capaUrl) {
        this.capaUrl = capaUrl;
    }

    public String getInformacaoTitulo() {
        return informacaoTitulo;
    }

    public void setInformacaoTitulo(String informacaoTitulo) {
        this.informacaoTitulo = informacaoTitulo;
    }

    public String getInformacaoDescricao() {
        return informacaoDescricao;
    }

    public void setInformacaoDescricao(String informacaoDescricao) {
        this.informacaoDescricao = informacaoDescricao;
    }

    public List<String> getCapturas() {
        return capturas;
    }

    public void setCapturas(List<String> capturas) {
        this.capturas = capturas;
    }

    public List<UserId> getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(List<UserId> curtidas) {
        this.curtidas = curtidas;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<RequisitosDeSistema> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<RequisitosDeSistema> requisitos) {
        this.requisitos = requisitos;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}
