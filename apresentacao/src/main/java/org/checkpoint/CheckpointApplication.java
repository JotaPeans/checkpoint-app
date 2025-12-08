package org.checkpoint;

import org.checkpoint.dominio.autenticacao.Autenticacao;
import org.checkpoint.dominio.autenticacao.AutenticacaoTemplate;
import org.checkpoint.dominio.comentario.ComentarioRepositorio;
import org.checkpoint.dominio.comentario.ComentarioServico;
import org.checkpoint.dominio.diario.DiarioRepositorio;
import org.checkpoint.dominio.diario.DiarioServico;
import org.checkpoint.dominio.email.EmailStrategy;
import org.checkpoint.dominio.email.EmailServico;
import org.checkpoint.dominio.email.VerificacaoEmailRepositorio;
import org.checkpoint.dominio.jogo.JogoRepositorio;
import org.checkpoint.dominio.jogo.JogoServico;
import org.checkpoint.dominio.lista.ListaJogosRepositorio;
import org.checkpoint.dominio.lista.ListaServico;
import org.checkpoint.dominio.user.UserRepositorio;
import org.checkpoint.dominio.user.UserServico;
import org.checkpoint.persistencia.jpa.autenticacao.AutenticacaoImpl;
import org.checkpoint.persistencia.jpa.email.ResendStrategy;
import org.checkpoint.utils.AuthUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.checkpoint")
@EnableJpaRepositories(basePackages = "org.checkpoint.persistencia.jpa")
@EntityScan(basePackages = "org.checkpoint.persistencia.jpa")
@ComponentScan(basePackages = {"org.checkpoint"})
public class CheckpointApplication {
    @Bean
    AuthUtil auth(AutenticacaoTemplate autenticacao) {
        return new AuthUtil(autenticacao);
    }

    @Bean
    EmailStrategy resendStrategy() {
        return new ResendStrategy();
    }

    @Bean
    EmailServico emailServico(VerificacaoEmailRepositorio repositorio) {
        return new EmailServico(repositorio, resendStrategy());
    }

    @Bean
    DiarioServico diarioServico(DiarioRepositorio repositorio) {
        return new DiarioServico(repositorio);
    }

    @Bean
    UserServico userServico(UserRepositorio repositorio, DiarioRepositorio diarioRepositorio,
                            VerificacaoEmailRepositorio verificacaoEmailRepositorio, Autenticacao autenticacao) {
        return new UserServico(repositorio, diarioRepositorio,
                new EmailServico(verificacaoEmailRepositorio, resendStrategy()), autenticacao);
    }

    @Bean
    JogoServico jogoServico(JogoRepositorio jogoRepositorio) {
        return new JogoServico(jogoRepositorio);
    }

    @Bean
    ComentarioServico comentarioServico(ComentarioRepositorio comentarioRepositorio) {
        return new ComentarioServico(comentarioRepositorio);
    }

    @Bean
    ListaServico listaServico(ListaJogosRepositorio listaJogosRepositorio, UserRepositorio userRepositorio) {
        return new ListaServico(listaJogosRepositorio, userRepositorio);
    }

    public static void main(String[] args) {
        SpringApplication.run(CheckpointApplication.class, args);
    }
}