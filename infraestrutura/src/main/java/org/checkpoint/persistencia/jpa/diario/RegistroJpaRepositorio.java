package org.checkpoint.persistencia.jpa.diario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroJpaRepositorio extends JpaRepository<RegistroJpa, Integer> {
    List<RegistroJpa> findByDiarioId(Integer diarioId);
}
