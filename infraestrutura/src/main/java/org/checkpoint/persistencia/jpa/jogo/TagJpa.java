package org.checkpoint.persistencia.jpa.jogo;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "TAG_JOGO")
public class TagJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne
    @JoinColumn(name = "jogo_id", nullable = false)
    public JogoJpa jogo;

    public String nome;
    public List<Integer> votos;
}
