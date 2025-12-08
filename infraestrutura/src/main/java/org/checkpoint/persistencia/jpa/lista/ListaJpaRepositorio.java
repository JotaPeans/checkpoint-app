package org.checkpoint.persistencia.jpa.lista;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListaJpaRepositorio extends JpaRepository<ListaJpa, Integer> {
    Optional<ListaJpa> findByTituloAndDono_Id(String titulo, Integer donoId);
    List<ListaJpa> findByDono_Id(Integer donoId);

    List<ListaJpa> findAllByIsPrivateFalseAndDono_IdNot(Integer donoId);
}
