package org.checkpoint.dominio.diario;

import java.util.Date;

import static org.apache.commons.lang3.Validate.notNull;

public class Conquista {
    private final ConquistaId id;

    private RegistroDiario registroDiario;
    private String nome;
    private Date dataDesbloqueada;
    private boolean concluida;

    public Conquista(String nome, Date dataDesbloqueada, boolean concluida, RegistroDiario registroDiario) {
        notNull(nome, "O nome não pode ser nulo");

        this.id = null;

        setNome(nome);
        setConcluida(concluida);
        setDataDesbloqueada(dataDesbloqueada);
        setRegistroDiario(registroDiario);
    }

    public Conquista(ConquistaId id, String nome, Date dataDesbloqueada, boolean concluida, RegistroDiario registroDiario) {
        notNull(id, "O id não pode ser nulo");
        notNull(nome, "O nome não pode ser nulo");

        this.id = id;

        setNome(nome);
        setDataDesbloqueada(dataDesbloqueada);
        setConcluida(concluida);
        setRegistroDiario(registroDiario);
    }

    public ConquistaId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataDesbloqueada() {
        return dataDesbloqueada;
    }

    public void setDataDesbloqueada(Date dataDesbloqueada) {
        this.dataDesbloqueada = dataDesbloqueada;
    }

    public boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    public RegistroDiario getRegistroDiario() {
        return registroDiario;
    }

    public void setRegistroDiario(RegistroDiario registroDiario) {
        this.registroDiario = registroDiario;
    }
}
