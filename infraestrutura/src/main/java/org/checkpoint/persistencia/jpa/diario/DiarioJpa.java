package org.checkpoint.persistencia.jpa.diario;

import java.util.Set;

import org.checkpoint.persistencia.jpa.user.UserJpa;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DIARIO")
public class DiarioJpa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int id;

  @OneToOne()
  @JoinColumn(name = "dono_id")
  public UserJpa dono;

  @OneToMany(mappedBy = "diario", fetch = FetchType.EAGER)
  public Set<RegistroJpa> registros;
}
