package org.checkpoint.persistencia.jpa.email;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VERIFICACAO_EMAIL")
public class VerificacaoEmailJpa {
  @Id
  public String token;

  public Integer userId;

  public Date dataExpiracao;
}
