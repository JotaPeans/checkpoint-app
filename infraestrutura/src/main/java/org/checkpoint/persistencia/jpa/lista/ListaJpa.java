package org.checkpoint.persistencia.jpa.lista;

import jakarta.persistence.*;
import org.checkpoint.persistencia.jpa.user.UserJpa;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "LISTAS")
public class ListaJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false)
    public UserJpa dono;

    public String titulo;
    public boolean isPrivate;

    public Set<Integer> jogos = new HashSet<>();
    public Set<Integer> curtidas = new HashSet<>();
}
