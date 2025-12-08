package org.checkpoint.apresentacao.diario;

import java.util.Date;

public record AddConquistaDTO(int registroId, String nome, Date dataDesbloqueada, boolean concluida) {
}
