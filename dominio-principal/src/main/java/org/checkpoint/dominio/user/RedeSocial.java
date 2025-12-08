package org.checkpoint.dominio.user;

import java.util.Objects;

public class RedeSocial {
    private String plataforma;
    private String username;

    public RedeSocial() {} // Jackson usa isso

    public RedeSocial(String plataforma, String username) {
        this.plataforma = plataforma;
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RedeSocial redeSocial) {
            return Objects.equals(this.plataforma, redeSocial.plataforma) && Objects.equals(this.username, redeSocial.username);
        }
        return false;
    }

    public String getPlataforma() {
        return this.plataforma;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }

    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return this.username;
    }
}
