package org.checkpoint.persistencia.jpa.jogo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagJpaRepositorio extends JpaRepository<TagJpa, Integer> {
    TagJpa findByNome(String nome);
}
