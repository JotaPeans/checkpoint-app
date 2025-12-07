package org.checkpoint.persistencia.jpa.jogo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequisitosDeSistemaJpaRepositorio extends JpaRepository<RequisitosDeSistemaJpa, Integer> {
    List<RequisitosDeSistemaJpa> findByJogoId(Integer id);
}
