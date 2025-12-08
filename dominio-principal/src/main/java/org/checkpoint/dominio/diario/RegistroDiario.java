package org.checkpoint.dominio.diario;

import org.checkpoint.dominio.jogo.JogoId;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class RegistroDiario {
    private final RegistroId id;

    private JogoId jogoId;
    private Diario diario;
    private Date dataInicio;
    private Date dataTermino;
    private List<Conquista> conquistas;

    public RegistroDiario(
        JogoId jogoId,
        Diario diario,
        Date dataInicio,
        Date dataTermino
    ) {
        notNull(jogoId, "O id do jogo não pode ser nulo");
        notNull(diario, "O id do diário não pode ser nulo");

        this.id = null;

        setJogoId(jogoId);
        setDataInicio(new Date());
        setDataInicio(dataInicio);
        setDataTermino(dataTermino);
    }
    
    public RegistroDiario(
            RegistroId id,
            JogoId jogoId,
            Diario diario,
            Date dataInicio,
            Date dataTermino,
            List<Conquista> conquistas
    ) {
        notNull(id, "O id não pode ser nulo");
        notNull(jogoId, "O id do jogo não pode ser nulo");
        notNull(dataInicio, "A data de início não pode ser nula");
        notNull(conquistas, "A lista de conquistas não pode ser nula");

        this.id = id;

        setJogoId(jogoId);
        setDiario(diario);
        setDataInicio(dataInicio);
        setDataTermino(dataTermino);
        setConquistas(conquistas);
    }

    public RegistroId getId() {
        return id;
    }

    public JogoId getJogoId() {
        return jogoId;
    }

    public void setJogoId(JogoId jogoId) {
        this.jogoId = jogoId;
    }

    public Diario getDiario() {
        return diario;
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }


    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public List<Conquista> getConquistas() {
        return conquistas;
    }

    public void setConquistas(List<Conquista> conquistas) {
        this.conquistas = conquistas;
    }
}
