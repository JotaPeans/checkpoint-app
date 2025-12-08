package org.checkpoint.persistencia.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepositorio extends JpaRepository<UserJpa, Integer> {
  UserJpa findUserJpaByEmail(String email);
}
