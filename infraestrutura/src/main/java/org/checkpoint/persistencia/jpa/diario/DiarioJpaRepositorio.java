package org.checkpoint.persistencia.jpa.diario;

import org.checkpoint.persistencia.jpa.user.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiarioJpaRepositorio extends JpaRepository<DiarioJpa, Integer> {
  DiarioJpa findDiarioJpaByDono(UserJpa id);
}
