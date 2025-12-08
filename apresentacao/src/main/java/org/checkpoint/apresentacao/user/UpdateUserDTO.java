package org.checkpoint.apresentacao.user;

import org.checkpoint.dominio.user.RedeSocial;

import java.util.List;

public record UpdateUserDTO(String nome, String bio, List<RedeSocial> redesSociais) {

}
