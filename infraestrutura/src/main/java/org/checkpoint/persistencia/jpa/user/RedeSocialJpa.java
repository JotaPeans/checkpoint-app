package org.checkpoint.persistencia.jpa.user;

import jakarta.persistence.Embeddable;

@Embeddable
public class RedeSocialJpa {
    public String plataforma;
    public String username;
}
