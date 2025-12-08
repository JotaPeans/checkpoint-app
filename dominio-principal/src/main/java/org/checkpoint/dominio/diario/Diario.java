package org.checkpoint.dominio.diario;

import org.checkpoint.dominio.user.UserId;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class Diario {
    private final DiarioId id;

    private UserId donoId;
    private List<RegistroDiario> registros;

    public Diario(UserId donoId) {
        notNull(donoId, "O id do dono do diário não pode ser nulo");

        this.id = null;

        setDonoId(donoId);
    }

    public Diario(DiarioId id, UserId donoId, List<RegistroDiario> registros) {
        notNull(id, "O id não pode ser nulo");
        notNull(donoId, "O id do dono do diário não pode ser nulo");
        notNull(registros, "A lista de registros não pode ser nulo");

        this.id = id;

        setDonoId(donoId);
        setRegistros(registros);
    }

    public DiarioId getId() {
        return id;
    }

    public UserId getDonoId() {
        return donoId;
    }

    public void setDonoId(UserId dono) {
        this.donoId = dono;
    }

    public List<RegistroDiario> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroDiario> registros) {
        this.registros = registros;
    }
}
