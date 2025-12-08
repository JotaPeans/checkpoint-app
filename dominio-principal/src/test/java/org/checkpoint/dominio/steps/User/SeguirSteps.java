package org.checkpoint.dominio.steps.User;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.user.User;

import static org.junit.jupiter.api.Assertions.*;

public class SeguirSteps extends CheckpointFuncionalidade {

    private final String nomeLari = "Lari";
    private final String nomeDavi = "Davi";
    private final String senhaLari = "Larisenha2%";
    private final String senhaDavi = "Davisenha2%";
    private final String emailLari = "lss2@cesar.school";
    private final String emailDavi = "druy@cesar.school";

    User lari;
    User davi;
    String notification;

    @Given("que o usuário {string} não segue {string}")
    public void queOUsuarioNaoSegue(String nome1, String nome2) {
        userServico.registerUser(emailLari, senhaLari, nome1);
        userServico.registerUser(emailDavi, senhaDavi, nome2);

        lari = repository.getByEmail(emailLari);
        davi = repository.getByEmail(emailDavi);
    }

    @And("o perfil de Davi é {string}")
    public void oPerfilDeDaviE(String visibilidade) {
        boolean isPrivate = visibilidade.equalsIgnoreCase("privado");
        userServico.togglePrivacidade(davi, isPrivate);
    }

    @When("{string} solicita seguir {string}")
    public void solicitaSeguir(String nomeSeguidor, String nomeAlvo) {
        userServico.toggleSeguir(lari, davi.getUserId());
    }

    @Then("o sistema registra imediatamente o follow")
    public void oSistemaRegistraImediatamenteOFolow() {
        assertTrue(davi.getSeguidores().contains(lari.getUserId()));
        assertTrue(lari.getSeguindo().contains(davi.getUserId()));

        assertTrue(davi.getSolicitacoesPendentes().isEmpty());
    }

    @Then("o sistema registra a solicitação como pendente")
    public void oSistemaRegistraASolicitacaoComoPendente() {
        assertFalse(davi.getSeguidores().contains(lari.getUserId()));
        assertFalse(lari.getSeguindo().contains(davi.getUserId()));
        assertTrue(davi.getSolicitacoesPendentes().contains(lari.getUserId()));
    }

    @Given("que o usuário {string} já segue {string}")
    public void queOUsuarioJaSegue(String nomeSeguidor, String nomeSeguido) {
        userServico.registerUser(emailLari, senhaLari, nomeSeguidor);
        userServico.registerUser(emailDavi, senhaDavi, nomeSeguido);

        lari = repository.getByEmail(emailLari);
        davi = repository.getByEmail(emailDavi);

        userServico.togglePrivacidade(davi, false);

        userServico.toggleSeguir(lari, davi.getUserId());
    }

    @When("ele da unfollow")
    public void eleDaUnfollow() {
        try {
            userServico.toggleSeguir(lari, davi.getUserId());
        } catch (Exception e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema atualiza os seguidores")
    public void oSistemaAtualizaOsSeguidores() {
        assertFalse(davi.getSeguidores().contains(lari.getUserId()));

        assertFalse(lari.getSeguindo().contains(davi.getUserId()));
    }

    @Given("que o usuário {string} enviou solicitação para seguir {string}")
    public void queOUsuarioEnviouSolicitacaoParaSeguir(String nomeSeguidor, String nomeSeguido) {
        userServico.registerUser(emailLari, senhaLari, nomeSeguidor);
        userServico.registerUser(emailDavi, senhaDavi, nomeSeguido);

        lari = repository.getByEmail(emailLari);
        davi = repository.getByEmail(emailDavi);

        userServico.togglePrivacidade(davi, true);

        userServico.toggleSeguir(lari, davi.getUserId());
    }

    @When("{string} aprova a solicitação")
    public void aprovaASolicitacao(String nome) {
        try {
            userServico.approveSeguidor(davi, lari.getUserId());
        } catch (Exception e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema adiciona {string} aos seguidores de {string}")
    public void oSistemaAdicionaAosSeguidores(String nomeSeguidor, String nomeSeguido) {
        assertTrue(davi.getSeguidores().contains(lari.getUserId()));
        assertTrue(lari.getSeguindo().contains(davi.getUserId()));
    }

    @And("remove a solicitação pendente")
    public void removeASolicitacaoPendente() {
        assertFalse(davi.getSolicitacoesPendentes().contains(lari.getUserId()));
    }

    @When("{string} rejeita a solicitação")
    public void rejeitaASolicitacao(String nome) {
        try {
            userServico.rejectSeguidor(davi, lari.getUserId());
        } catch (Exception e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema remove a solicitação pendente")
    public void oSistemaRemoveASolicitacaoPendente() {
        assertFalse(davi.getSolicitacoesPendentes().contains(lari.getUserId()));
    }

    @And("{string} continua sem seguir {string}")
    public void continuaSemSeguir(String nomeSeguidor, String nomeSeguido) {
        assertFalse(lari.getSeguindo().contains(davi.getUserId()));
        assertFalse(davi.getSeguidores().contains(lari.getUserId()));
    }
}