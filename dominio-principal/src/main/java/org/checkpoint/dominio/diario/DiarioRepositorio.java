package org.checkpoint.dominio.diario;

import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.User;

import java.util.Date;
import java.util.List;

public interface DiarioRepositorio {
    Diario saveDiario(Diario diario);

    Diario getDiario(DiarioId id);

    Diario getDiarioByDono(User user);

    RegistroDiario createRegistroDiario(JogoId jogo, Diario diario, Date dataInicio, Date dataTermino);
    List<RegistroDiario> listRegistros(DiarioId diarioId);
    RegistroDiario getRegistroDiario(RegistroId registroId);
    void saveRegistroDiario(RegistroDiario registroDiario);
    void removeRegistroDiario(RegistroId registroId);

    Conquista createConquista(String nome, Date dataDesbloqueada, boolean isUnloked, RegistroId registroId);
    List<Conquista> listConquistas(RegistroId registroId);
    Conquista getConquista(ConquistaId conquistaId);
    void saveConquista(Conquista conquista);
    void removeConquista(ConquistaId conquistaId);
}
