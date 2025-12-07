package org.checkpoint.persistencia.jpa.jogo;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AVALIACAO_JOGO")
public class AvaliacaoJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public Integer autorId;

    @ManyToOne
    @JoinColumn(name = "jogo_id", nullable = false)
    public JogoJpa jogo;

    public Double nota;
    public String comentario;
    public Date data;

    public Set<Integer> curtidas = new HashSet<>();
}
