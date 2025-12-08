package org.checkpoint.dominio.steps.User;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.email.VerificacaoEmail;
import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.email.Token;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CadastroSteps extends CheckpointFuncionalidade {

    private String nomeLari = "Lari";
    private String senhaLari = "Larisenha2%";
    private String emailLari = "lss2@cesar.school";
    private String notification;

    @Given("que não existe um usuário com o e-mail {string} no sistema")
    public void queNãoExisteUmUsuárioComOEMailNoSistema(String email) {
        assertFalse(userServico.isEmailAlreadyInUse(email));
    }

    @Given("que o e-mail {string} já esteja cadastrado no sistema")
    public void queOEMailJáEstejaCadastradoNoSistema(String email) {
        userServico.registerUser(email, senhaLari, nomeLari);
    }

    @Given("que o usuário com email {string} forneceu o token de verificacao dentro de {int} hora depois do envio")
    public void queOUsuárioComEmailForneceuOTokenDeVerificacaoDentroDeHoraDepoisDoEnvio(String email, int arg0) {
        userServico.registerUser(email, senhaLari, nomeLari);
        User user = repository.getByEmail(email);
        VerificacaoEmail ver = repository.getByUser(user);
        Token token = ver.getToken();
        userServico.verifyUserEmail(token);
    }

    @Given("que o usuário com email {string} não forneceu o token de verificacao")
    public void queOUsuárioComEmailNãoForneceuOTokenDeVerificacao(String email) {
        userServico.registerUser(email, senhaLari, nomeLari);
    }

    @When("o usuário termina o processo de cadastro")
    public void oUsuárioTerminaOProcessoDeCadastro() {
        try {
            notification = userServico.registerUser(emailLari, senhaLari, nomeLari);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @When("a senha {string} é fornecida pelo usuario")
    public void aSenhaÉFornecidaPeloUsuario(String senha) {
        try {
            notification = userServico.registerUser(emailLari, senha, nomeLari);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @When("o email {string} é fornecido no cadastro")
    public void oEmailÉFornecidoNoCadastro(String email) {
        try {
            notification = userServico.registerUser(email, senhaLari, nomeLari);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @When("o usuário tenta fazer login")
    public void oUsuárioTentaFazerLogin() {
        try {
            notification = userServico.login(emailLari, senhaLari);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema notifica que O email de verificação com o token foi enviado para o email")
    public void oSistemaNotificaQueOEmailDeVerificaçãoComOTokenFoiEnviadoParaOEmail() {
        assertEquals("O email de verificação com o token foi enviado para o email", notification);
    }

    @Then("o sistema notifica que O e-mail já está cadastrado")
    public void oSistemaNotificaQueOEMailJáEstáCadastrado() {
        assertEquals("O e-mail já está cadastrado", notification);
    }

    @Then("o sistema notifica que O e-mail está fora do padrão")
    public void oSistemaNotificaQueOEMailEstáForaDoPadrão() {
        assertEquals("O e-mail está fora do padrão", notification);
    }

    @Given("que o usuario tenta fazer o cadastro com a senha {string} e nome {string}")
    public void queOUsuarioTentaFazerOCadastroComASenhaENome(String senha, String nome) {
        try {
            userServico.registerUser(null, senha, nome);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Given("que o usuario tenta fazer o cadastro com o email {string} e nome {string}")
    public void queOUsuarioTentaFazerOCadastroComOEmailENome(String email, String nome) {
        try {
            userServico.registerUser(email, null, nome);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Given("que o usuario tenta fazer o cadastro com o email {string} e senha {string}")
    public void queOUsuarioTentaFazerOCadastroComOEmailESenha(String email, String senha) {
        try {
            userServico.registerUser(email, senha, null);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema notifica que O email não pode ser nulo")
    public void oSistemaNotificaQueOEmailNãoPodeSerNulo() {
        assertEquals("O email não pode ser nulo", notification);
    }

    @Then("o sistema notifica que A senha não pode ser nula")
    public void oSistemaNotificaQueASenhaNãoPodeSerNula() {
        assertEquals("A senha não pode ser nula", notification);
    }

    @Then("o sistema notifica que O nome não pode ser nulo")
    public void oSistemaNotificaQueONomeNãoPodeSerNulo() {
        assertEquals("O nome não pode ser nulo", notification);
    }

    @Then("o sistema notifica que A conta não foi verificada")
    public void oSistemaNotificaQueAContaNãoFoiVerificada() {
        assertEquals("A conta não foi verificada", notification);
    }

    @Then("o sistema notifica que A senha deve conter, no mínimo, pelo menos uma letra minúscula, pelo menos uma letra maiúscula, pelo menos um número, pelo menos um caracter especial, e pelo menos {int} digitos")
    public void oSistemaNotificaQueASenhaDeveConterNoMínimoPeloMenosUmaLetraMinúsculaPeloMenosUmaLetraMaiúsculaPeloMenosUmNúmeroPeloMenosUmCaracterEspecialEPeloMenosDigitos(
            int arg0) {
        assertEquals(
                "A senha deve conter, no mínimo, pelo menos uma letra minúscula, pelo menos uma letra maiúscula, pelo menos um número, pelo menos um caracter especial, e pelo menos 8 digitos",
                notification);
    }

    @Given("que o usuário com email {string} fornece o token de verificacao depois de {int} hora")
    public void queOUsuárioComEmailForneceOTokenDeVerificacaoDepoisDeHora(String email, int horas) {
        userServico.registerUser(email, senhaLari, nomeLari);
        User user = repository.getByEmail(email);
        VerificacaoEmail ver = repository.getByUser(user);
        long millis = System.currentTimeMillis() - (horas * 60L * 60L * 1000L);
        ver.setDataExpiracao(new Date(millis));
        Token token = ver.getToken();
        try {
            userServico.verifyUserEmail(token);
            ;
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema notifica que O token está expirado")
    public void oSistemaNotificaQueOTokenEstáExpirado() {
        assertEquals("O token está expirado", notification);
    }

    @Then("o sistema retorna o token de autenticação do usuário")
    public void oSistemaRetornaOTokenDeAutenticaçãoDoUsuário() {
        assertEquals("token jwt", notification);
    }
}