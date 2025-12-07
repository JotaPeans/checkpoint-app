package org.checkpoint.persistencia.jpa.jogo;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "JOGO")
public class JogoJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String nome;
    public String company;
    public String capaUrl;
    public String informacaoTitulo;
    public String informacaoDescricao;
    public double nota;

    public Set<String> capturas = new HashSet<>();

    public Set<Integer> curtidas = new HashSet<>();

    @OneToMany(mappedBy = "jogo", fetch = FetchType.EAGER)
    public Set<TagJpa> tags = new HashSet<>();

    @OneToMany(mappedBy = "jogo", fetch = FetchType.EAGER)
    public Set<RequisitosDeSistemaJpa> requisitos = new HashSet<>();

    @OneToMany(mappedBy = "jogo", fetch = FetchType.LAZY)
    public Set<AvaliacaoJpa> avaliacoes = new HashSet<>();
}
