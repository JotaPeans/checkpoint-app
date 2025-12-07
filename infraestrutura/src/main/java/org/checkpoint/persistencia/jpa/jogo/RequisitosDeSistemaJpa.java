package org.checkpoint.persistencia.jpa.jogo;

import jakarta.persistence.*;

@Entity
@Table(name = "REQUISITOS_DE_SISTEMA")
public class RequisitosDeSistemaJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne
    @JoinColumn(name = "jogo_id", nullable = false)
    public JogoJpa jogo;

    public String sistemaOp;
    public String processador;
    public String memoria;
    public String placaVideo;
    public String tipo;
}
