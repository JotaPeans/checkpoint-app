package org.checkpoint.dominio;

import org.checkpoint.dominio.email.EmailServico;
import org.checkpoint.dominio.comentario.ComentarioServico;
import org.checkpoint.dominio.diario.DiarioServico;
import org.checkpoint.dominio.jogo.JogoServico;
import org.checkpoint.dominio.lista.ListaServico;
import org.checkpoint.dominio.user.UserServico;
import org.checkpoint.infraestrutura.persistencia.memoria.Repositorio;

public class CheckpointFuncionalidade {
    protected UserServico userServico;
    protected JogoServico jogoServico;
    protected DiarioServico diarioServico;
    protected EmailServico emailServico;
    protected ListaServico listaServico;
    protected ComentarioServico comentarioServico;

    protected Repositorio repository = new Repositorio();

    public CheckpointFuncionalidade() {
        emailServico = new EmailServico(repository, repository);
        userServico = new UserServico(repository, repository, emailServico, repository);
        listaServico = new ListaServico(repository, repository);
        jogoServico = new JogoServico(repository);
        diarioServico = new DiarioServico(repository);
        comentarioServico = new ComentarioServico(repository);
    }
}
