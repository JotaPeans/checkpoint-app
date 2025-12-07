package org.checkpoint.persistencia.jpa.jogo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoJpaRepositorio extends JpaRepository<AvaliacaoJpa, Integer> {
    List<AvaliacaoJpa> findByJogoId(Integer jogoId);
}
