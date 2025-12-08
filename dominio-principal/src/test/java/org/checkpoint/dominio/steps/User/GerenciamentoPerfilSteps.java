package org.checkpoint.dominio.steps.User;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.user.RedeSocial;
import org.checkpoint.dominio.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GerenciamentoPerfilSteps extends CheckpointFuncionalidade {

    private String nomeLari = "Lari";
    private String nomeJp= "JP";
    private String senhaLari = "Larisenha2%";
    private String senhaJp = "Jpppppsenha2%";
    private String emailLari = "lss2@cesar.school";
    private String emailJp = "jotapeans@cesar.school";
    private String urlAntigo;

    User lari;
    User jp;


    private String notification;

    @Given("que o usuário {string} tem o perfil {string}")
    public void queOUsuárioTemOPerfil(String nome, String status) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        userServico.registerUser(emailJp, senhaJp, nomeJp);
        jp = repository.getByEmail(emailJp);

        if (status.equals("privado")) {
            lari.setIsPrivate(true);
        }
    }

    @Given("que existe uma solicitação pendente do usuário {string} para {string}")
    public void queExisteUmaSolicitaçãoPendenteDoUsuárioPara(String nomeSolicitante, String nomeSolicitado) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);
        lari = repository.getByEmail(emailLari);
        lari.setIsPrivate(true); // garante que o perfil é privado

        userServico.registerUser(emailJp, senhaJp, nomeJp);
        jp = repository.getByEmail(emailJp);

        userServico.toggleSeguir(jp, lari.getUserId());
    }

    @Then("o sistema deve atualizar o avatar, substituindo a imagem anterior")
    public void oSistemaDeveAtualizarOAvatarSubstituindoAImagemAnterior() {
        assertNotSame(lari.getAvatarUrl(), urlAntigo);
    }

    @Then("o sistema notifica que O formato não é permitido")
    public void oSistemaNotificaQueOFormatoNãoÉPermitido() {
        assertEquals("O formato não é permitido", notification);
    }

    @Then("o sistema notifica que O arquivo excede o tamanho máximo permitido")
    public void oSistemaNotificaQueOArquivoExcedeOTamanhoMáximoPermitido() {
        assertEquals("O arquivo excede o tamanho máximo permitido", notification);
    }

    @Then("apenas seguidores aprovados podem visualizar as informações do perfil")
    public void apenasSeguidoresAprovadosPodemVisualizarAsInformaçõesDoPerfil() {
        try {
            notification = userServico.getInformacoes(jp, lari.getUserId()).getEmail();
        } catch (Exception ex){
            notification = ex.getMessage();
        }

        assertNull(notification);
    }

    @Then("qualquer seguidor pode visualizar as informações do perfil")
    public void qualquerSeguidorPodeVisualizarAsInformaçõesDoPerfil() {
        try {
            notification = userServico.getInformacoes(jp, lari.getUserId()).getEmail();
        } catch (Exception ex){
            notification = ex.getMessage();
        }

        assertNotNull(notification);
    }

    @When("o usuário {string} solicita para seguir o perfil")
    public void oUsuárioSolicitaParaSeguirOPerfil(String nomeSolicitante) {
        User solicitante = nomeSolicitante.equals(nomeJp) ? jp : lari;
        userServico.toggleSeguir(solicitante, lari.getUserId());
    }

    @When("Lari {string} a solicitação")
    public void LariASolicitacao( String acao) {
        if (acao.equalsIgnoreCase("aceita")) {
            userServico.approveSeguidor(lari, jp.getUserId());
        } else if (acao.equalsIgnoreCase("rejeita")) {
            userServico.rejectSeguidor(lari, jp.getUserId());
        }
    }

    @Then("a solicitação deve ficar pendente até aprovação ou rejeição manual")
    public void aSolicitaçãoDeveFicarPendenteAtéAprovaçãoOuRejeiçãoManual() {
        assertTrue(lari.getSolicitacoesPendentes().contains(jp.getUserId()));
        try {
            notification = userServico.getInformacoes(jp, lari.getUserId()).getEmail();
        } catch (Exception ex){
            notification = ex.getMessage();
        }

        assertNull(notification);
    }

    @Then("{string} passa a ter acesso às informações do perfil")
    public void passaATerAcessoÀsInformaçõesDoPerfil(String arg0) {
        assertTrue(lari.getSeguidores().contains(jp.getUserId()));

        try {
            notification = userServico.getInformacoes(jp, lari.getUserId()).getEmail();
        } catch (Exception ex){
            notification = ex.getMessage();
        }

        assertNotNull(notification);
    }

    @Then("{string} não terá acesso às informações do perfil")
    public void nãoTeráAcessoÀsInformaçõesDoPerfil(String arg0) {
        assertFalse(lari.getSeguidores().contains(jp.getUserId()));

        try {
            notification = userServico.getInformacoes(jp, lari.getUserId()).getEmail();
        } catch (Exception ex){
            notification = ex.getMessage();
        }

        assertNull(notification);
    }

    @Given("que o usuário {string} inicia a atualização do avatar enviando a url {string}")
    public void queOUsuárioIniciaAAtualizaçãoDoAvatarEnviandoAUrl(String nome, String url) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        urlAntigo = lari.getAvatarUrl();

        userServico.updateAvatar(lari, url);
    }

    @Given("Given que o usuário {string} inicia a atualização da bio para {string}")
    public void givenQueOUsuárioIniciaAAtualizaçãoDaBioPara(String nome, String bio) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        lari.setBio(bio);
    }

    @Then("a bio do usuário Lari deve ser {string}")
    public void aBioDoUsuárioLariDeveSer(String bio) {
        assertEquals(lari.getBio(),bio);
    }

    @Given("Given que o usuário {string} inicia a atualização das redes sociais para {string} na plataforma {string}")
    public void givenQueOUsuárioIniciaAAtualizaçãoDasRedesSociaisParaNaPlataforma(String nome, String user, String plataforma) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        List<RedeSocial> redesSociaisAntigas = lari.getRedesSociais();

        RedeSocial newRedeSocial = new RedeSocial(plataforma, user);

        redesSociaisAntigas.add(newRedeSocial);

        lari.setRedesSociais(redesSociaisAntigas);
    }

    @Then("as redes sociais do usuário Lari devem conter {string}")
    public void asRedesSociaisDoUsuárioLariDevemConter(String usernameEsperado) {
        boolean contem = lari.getRedesSociais().stream()
                .anyMatch(rede -> rede.getUsername().equals(usernameEsperado));

        assertTrue(contem);
    }
}