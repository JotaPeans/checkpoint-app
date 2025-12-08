package org.checkpoint.persistencia.jpa.diario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConquistaJpaRepositorio extends JpaRepository<ConquistaJpa, Integer> {
    List<ConquistaJpa> findByRegistroId(Integer registroId);
}
