package org.checkpoint.apresentacao.diario;

import java.util.Date;

public record UpdateConquistaDTO(String nome, Date dataDesbloqueada, boolean concluida) {
}
