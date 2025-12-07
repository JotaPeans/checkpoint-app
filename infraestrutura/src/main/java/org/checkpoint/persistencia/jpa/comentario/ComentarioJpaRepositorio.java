package org.checkpoint.persistencia.jpa.comentario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioJpaRepositorio extends JpaRepository<ComentarioJpa, Integer> {
    // 1. Buscar comentários que têm um comentário pai específico
    List<ComentarioJpa> findByComentarioPaiId(Integer comentarioPaiId);

    // 2. Buscar comentários raiz (sem pai) de uma avaliação específica
    List<ComentarioJpa> findByAvaliacaoAlvoIdAndComentarioPaiIdIsNull(Integer avaliacaoAlvoId);

    // 3. Buscar comentários raiz (sem pai) de uma lista específica
    List<ComentarioJpa> findByListaAlvoIdAndComentarioPaiIdIsNull(Integer listaAlvoId);
}
