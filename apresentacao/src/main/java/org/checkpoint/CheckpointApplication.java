package org.checkpoint;

import org.checkpoint.dominio.autenticacao.AutenticacaoTemplate;
import org.checkpoint.dominio.jogo.JogoRepositorio;
import org.checkpoint.dominio.jogo.JogoServico;
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
    JogoServico jogoServico(JogoRepositorio jogoRepositorio) {
        return new JogoServico(jogoRepositorio);
    }

    public static void main(String[] args) {
        SpringApplication.run(CheckpointApplication.class, args);
    }
}