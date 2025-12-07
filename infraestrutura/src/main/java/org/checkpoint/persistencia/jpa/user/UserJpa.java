package org.checkpoint.persistencia.jpa.user;

import jakarta.persistence.*;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.persistencia.jpa.diario.DiarioJpa;
import org.checkpoint.persistencia.jpa.diario.RegistroJpa;
import org.checkpoint.persistencia.jpa.lista.ListaJpa;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USUARIO")
public class UserJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String email;
    public String nome;
    public String senha;
    public String avatarUrl;

    @Column(length = 3000)
    public String bio;

    public boolean isPrivate;
    public boolean emailVerificado;

    @OneToOne(mappedBy = "dono", cascade = CascadeType.ALL)
    public DiarioJpa diario;

    public Set<Integer> solicitacoesPendentes = new HashSet<>();

    @ElementCollection
    public Set<RedeSocialJpa> redesSociais = new HashSet<>();

    public Set<Integer> seguindo = new HashSet<>();
    public Set<Integer> seguidores = new HashSet<>();

    @OneToMany(mappedBy = "dono", fetch = FetchType.EAGER)
    public Set<ListaJpa> listas = new HashSet<>();

    public Set<Integer> jogosFavoritos = new HashSet<>();
}
