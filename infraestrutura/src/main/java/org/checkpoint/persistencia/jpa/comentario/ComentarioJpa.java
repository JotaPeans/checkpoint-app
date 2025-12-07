package org.checkpoint.persistencia.jpa.comentario;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "COMENTARIO")
public class ComentarioJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public Integer autorId;
    public String conteudo;
    public Date data;
    public List<Integer> curtidas;

    public Integer avaliacaoAlvoId;
    public Integer listaAlvoId;

    public Integer comentarioPaiId;

}
