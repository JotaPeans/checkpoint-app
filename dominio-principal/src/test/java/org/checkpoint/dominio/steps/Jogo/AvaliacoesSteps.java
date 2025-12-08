package org.checkpoint.dominio.steps.Jogo;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.jogo.Avaliacao;
import org.checkpoint.dominio.jogo.AvaliacaoId;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AvaliacoesSteps extends CheckpointFuncionalidade {

    private String nomeLari = "Lari";
    private String senhaLari = "Larisenha2%";
    private String emailLari = "lss2@cesar.school";
    private String notification;

    User lari;
    Jogo jogo;
    private String ultimaCritica;

    private AvaliacaoId avaliacaoAlvoId;
    private int totalAvaliacoesAntesEdicao;
    private double mediaAntesEdicao;
    private String novaCritica;
    private double novaNota;

    private double notaAnterior;

    @Given("que o usuário {string} esta na página do jogo {string}")
    public void queOUsuárioEstaNaPáginaDoJogo(String nome, String nomeJogo) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);

        lari = repository.getByEmail(emailLari);

        JogoId jogoId = new JogoId(1);
        jogo = new Jogo(
                jogoId,
                nomeJogo,
                "DevStudio",
                "https://example.com/" + nome.toLowerCase() + ".jpg",
                "Descrição curta de " + nome,
                "Uma descrição longa e detalhada de " + nome,
                0,
                new ArrayList<>(), // capturas
                new ArrayList<>(), // curtidas
                new ArrayList<>(), // tags
                new ArrayList<>(), // requisitos
                new ArrayList<>()  // Avaliações
        );
        repository.saveJogo(jogo);
    }

    @When("ele atribui a nota {double}")
    public void eleAtribuiANota(Double nota) {
        notaAnterior = jogo.getNota();
        try {
            jogoServico.submitAvaliacao(lari, jogo.getId(), nota, null);
        }catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("a média geral do jogo deve ser atualizada")
    public void aMédiaGeralDoJogoDeveSerAtualizada() {
        assertNotEquals(jogo.getNota(), notaAnterior);
    }

    @Then("o sistema notifica que A nota tem que estar entre {double} e {int}")
    public void oSistemaNotificaQueANotaTemQueEstarEntreE(double arg0, int arg1) {
        assertEquals("A nota tem que estar entre 0.5 e 5", notification);
    }

    @When("ele atribui a nota {double} e a crítica {string}")
    public void eleAtribuiANotaEACrítica(double nota,  String critica) {
        try {
            ultimaCritica = critica;
            jogoServico.submitAvaliacao(lari, jogo.getId(), nota, critica);
        }catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @And("o comentário enviado")
    public void oComentárioEnviado() {
        List<Avaliacao> avaliacoes = repository.getAvaliacoesByJogoId(jogo.getId());

        Avaliacao ultima = avaliacoes.getLast();

        assertEquals(lari.getUserId(), ultima.getAutorId(), "Autor da avaliação não confere.");

        assertEquals(ultimaCritica, ultima.getComentario());
    }

    @Then("o sistema notifica que A crítica deve ter no mínimo {int} caracteres")
    public void oSistemaNotificaQueACríticaDeveTerNoMínimoCaracteres(int arg0) {
        assertEquals("A crítica deve ter no mínimo 20 caracteres", notification);
    }

    @Then("o sistema notifica que A crítica deve ter no máximo {int} caracteres")
    public void oSistemaNotificaQueACríticaDeveTerNoMáximoCaracteres(int arg0) {
        assertEquals("A crítica deve ter no máximo 2000 caracteres", notification);
    }

    @Then("o sistema notifica que A nota não pode ser nula")
    public void oSistemaNotificaQueANotaNãoPodeSerNula() {
        assertEquals("A nota não pode ser nula", notification);
    }

    @When("ele atribui a crítica {string}")
    public void eleAtribuiACrítica(String critica) {
        try {
            jogoServico.submitAvaliacao(lari, jogo.getId(), null, critica);
        }catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Given("que o usuário {string} inicia o processo de edição da avaliação no jogo {string}")
    public void queOUsuárioIniciaOProcessoDeEdiçãoDaAvaliaçãoNoJogo(String nome, String nomeJogo) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);

        lari = repository.getByEmail(emailLari);

        JogoId jogoId = new JogoId(1);
        jogo = new Jogo(
                jogoId,
                nomeJogo,
                "DevStudio",
                "https://example.com/" + nome.toLowerCase() + ".jpg",
                "Descrição curta de " + nome,
                "Uma descrição longa e detalhada de " + nome,
                0,
                new ArrayList<>(), // capturas
                new ArrayList<>(), // curtidas
                new ArrayList<>(), // tags
                new ArrayList<>(), // requisitos
                new ArrayList<>()  // Avaliações
        );
        repository.saveJogo(jogo);

        jogoServico.submitAvaliacao(lari, jogo.getId(), 4.0, "Minecraft é um jogo muito bom, super recomendo jogar!");
    }

    @When("ele altera a nota para {double} e a crítica para {string}")
    public void eleAlteraANotaParaEACríticaPara(double nota, String critica) {
        novaNota = nota;
        novaCritica = critica;

        List<Avaliacao> avaliacoes = repository.getAvaliacoesByJogoId(jogo.getId());

        Avaliacao alvo = avaliacoes.getLast();
        avaliacaoAlvoId = alvo.getId();

        totalAvaliacoesAntesEdicao = avaliacoes.size();
        mediaAntesEdicao = jogo.getNota();

        try {
            jogoServico.editAvaliacao(lari, avaliacaoAlvoId, nota, critica);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @And("o sistema deve salvar a nova versão da avaliação")
    public void oSistemaDeveSalvarANovaVersaoDaAvaliacao() {
        List<Avaliacao> depois = repository.getAvaliacoesByJogoId(jogo.getId());
        assertEquals(totalAvaliacoesAntesEdicao, depois.size());

        Avaliacao editada = repository.getAvaliacaoById(avaliacaoAlvoId);
        assertEquals(lari.getUserId(), editada.getAutorId());
        assertEquals(novaNota, editada.getNota(), 0.0001);
        assertEquals(novaCritica, editada.getComentario());

        // média do jogo deve ter sido recalculada (se fizer sentido com a base atual)
        assertNotEquals(mediaAntesEdicao, jogo.getNota());
    }

}
