package org.checkpoint.persistencia.jpa.diario;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "REGISTROS_DIARIO")
public class RegistroJpa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int id;

  public Integer jogoId;

  @OneToMany(mappedBy = "registro", fetch = FetchType.EAGER)
  public Set<ConquistaJpa> conquistas;

  @ManyToOne
  @JoinColumn(name = "diario_id", nullable = false)
  public DiarioJpa diario;

  public Date dataInicio;
  public Date dataTermino;
}
