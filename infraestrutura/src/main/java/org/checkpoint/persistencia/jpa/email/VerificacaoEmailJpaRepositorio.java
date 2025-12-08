package org.checkpoint.persistencia.jpa.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificacaoEmailJpaRepositorio extends JpaRepository<VerificacaoEmailJpa, String> {
  VerificacaoEmailJpa findVerificacaoEmailJpaByUserId(int userId);
  VerificacaoEmailJpa findVerificacaoEmailJpaByToken(String token);
}
