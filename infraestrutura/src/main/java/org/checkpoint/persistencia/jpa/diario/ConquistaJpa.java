package org.checkpoint.persistencia.jpa.diario;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONQUISTAS_DIARIO")
public class ConquistaJpa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int id;

  @ManyToOne
  @JoinColumn(name = "registro_id", nullable = false)
  public RegistroJpa registro;

  public String nome;
  public Date dataDesbloqueada;
  public boolean concluida;
}
