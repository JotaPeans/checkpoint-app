package org.checkpoint.dominio.steps.Jogo;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.jogo.Tag;
import org.checkpoint.dominio.jogo.TagId;
import org.checkpoint.dominio.steps.Common;
import org.checkpoint.dominio.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TagSteps extends CheckpointFuncionalidade {

    Common common = new Common();

    private String nomeLari = "Lari";
    private String nomeJp   = "JP";
    private String senhaLari = "Larisenha2%";
    private String senhaJp   = "Jpppppsenha2%";
    private String emailLari = "lss2@cesar.school";
    private String emailJp   = "jotapeans@cesar.school";
    private String notification;

    public Jogo jogo;
    private List<String> tagsEsperadas;
    private List<Tag> tagsAntes = new ArrayList<>();


    private User autorAtual;

    @Given("que o usuário {string} esteja na página do jogo {string}")
    public void usuarioEstaNaPagina(String nome, String nomeJogo) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);

        autorAtual = repository.getByEmail(emailLari);

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

    @When("ele adiciona as tags {string} ao jogo {string}")
    public void eleAdicionaAsTagsAoJogo(String tagsString, String jogoNome) {
        tagsEsperadas = common.extrairLista(tagsString);

        try {
            jogoServico.addTagsToGame(jogo.getId(), autorAtual.getUserId(), tagsEsperadas);
            notification = null;
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("as tags desse jogo serão atualizadas")
    public void tagsAtualizadas() {
        Jogo atualizado = jogoServico.getJogo(jogo.getId());
        List<Tag> idsNoJogo = atualizado.getTags();

        int unicasEsperadas = new HashSet<>(tagsEsperadas).size();
        assertEquals(unicasEsperadas, idsNoJogo.size(), "Quantidade de tags no jogo deve refletir itens únicos.");

        for (String nomeTag : new HashSet<>(tagsEsperadas)) {
            Tag tag = repository.getTagByName(nomeTag);
            assertNotNull(tag, "Tag deveria existir no repositório: " + nomeTag);
            assertTrue(idsNoJogo.contains(tag), "Tag deveria estar associada ao jogo: " + nomeTag);
        }
    }

    @Then("o sistema notifica que Não é permitido adicionar mais de {int} tags de uma vez.")
    public void oSistemaNotificaQueNãoÉPermitidoAdicionarMaisDeTagsDeUmaVez(int max) {
        assertEquals("Não é permitido adicionar mais de 20 tags de uma vez.", notification);
    }

    @Given("que o usuário {string} tenha adicionado a tag {string} ao jogo {string}")
    public void queOUsuárioTenhaAdicionadoATagAoJogo(String nome, String tagNome, String nomeJogo) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);

        autorAtual = repository.getByEmail(emailLari);

        JogoId jogoId = new JogoId(1);
        jogo = new Jogo(
                jogoId,
                nomeJogo,
                "DevStudio",
                "https://example.com/" + nome.toLowerCase() + ".jpg",
                "Descrição curta de " + nome,
                "Uma descrição longa e detalhada de " + nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        repository.saveJogo(jogo);

        tagsEsperadas = common.extrairLista(tagNome);
        jogoServico.addTagsToGame(jogo.getId(), autorAtual.getUserId(), tagsEsperadas);
    }

    @Then("o sistema ignora a duplicação e atualiza as tags")
    public void oSistemaIgnoraADuplicaçãoEAtualizaAsTags() {
        Jogo jogoAtualizado = repository.getJogo(jogo.getId());

        List<Tag> tagsDoJogo = jogoAtualizado.getTags();
        List<String> nomesDasTags = tagsDoJogo.stream()
                .map(Tag::getNome)
                .toList();

        long total   = nomesDasTags.size();
        long unicas  = new HashSet<>(nomesDasTags).size();
        assertEquals(unicas, total, "O jogo deve ter tags únicas (sem duplicar entradas).");

        for (String esperada : new HashSet<>(tagsEsperadas)) {
            assertTrue(nomesDasTags.contains(esperada), "Tag esperada não encontrada no jogo: " + esperada);
        }
    }

    @And("que o jogo {string} possua as tags {string}")
    public void queOJogoPossuaAsTags(String jogoNome, String tagsString) {
        List<String> tagsIniciais = common.extrairLista(tagsString);
        var lari = repository.getByEmail(emailLari) != null ? repository.getByEmail(emailLari) : null;
        if (lari == null) {
            userServico.registerUser(emailLari, senhaLari, nomeLari);
            lari = repository.getByEmail(emailLari);
        }
        jogoServico.addTagsToGame(jogo.getId(), lari.getUserId(), tagsIniciais);
    }

    @When("ele remove a tag {string} do jogo {string}")
    public void eleRemoveATagDoJogo(String nomeTag, String jogoNome) {
        try {
            jogoServico.removeTagFromGame(jogo.getId(), nomeTag, autorAtual.getUserId());
            notification = null;
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("a tag {string} não deve mais estar associada ao jogo")
    public void aTagNãoDeveMaisEstarAssociadaAoJogo(String nomeTag) {
        Jogo jogoAtualizado = repository.getJogo(jogo.getId());
        List<String> nomesAtuais = jogoAtualizado.getTags().stream()
                .map(Tag::getNome)
                .toList();

        assertFalse(nomesAtuais.contains(nomeTag), "A tag ainda está associada ao jogo (algum voto restou).");
    }

    @Then("o sistema notifica que Tag não encontrada")
    public void oSistemaNotificaQueTagNãoEncontrada() {
        assertEquals("Tag não encontrada", notification);
    }

    @Then("as {int} tags mais utilizadas vão ser exibidas na seção de tags")
    public void asTagsMaisUtilizadasVãoSerExibidasNaSeçãoDeTags(int qtd) {
        List<String> tagsLari = List.of(
                "RPG de ação",
                "Dificuldade elevada",
                "Atmosfera sombria",
                "Exploração",
                "História profunda"
        );
        jogoServico.addTagsToGame(jogo.getId(), autorAtual.getUserId(), tagsLari);

        var topAntes = jogoServico.getTopTags(jogo.getId());

        userServico.registerUser(emailJp, senhaJp, nomeJp);
        User jp = repository.getByEmail(emailJp);

        List<String> tagsJp = List.of(
                "RPG de ação",          // igual
                "Dificuldade elevada",  // igual
                "Exploração",           // igual
                "Aventura",             // nova
                "Multiplayer"           // nova
        );
        jogoServico.addTagsToGame(jogo.getId(), jp.getUserId(), tagsJp);

        var topDepois = jogoServico.getTopTags(jogo.getId());

        assertNotEquals(topDepois,topAntes);
    }

    @Given("que as tags {string} tenham a mesma contagem de no jogo {string}")
    public void queAsTagsTenhamAMesmaContagemDeNoJogo(String tags, String nomeJogo) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);

        autorAtual = repository.getByEmail(emailLari);

        JogoId jogoId = new JogoId(1);
        jogo = new Jogo(
                jogoId,
                nomeJogo,
                "DevStudio",
                "https://example.com/" + nomeLari.toLowerCase() + ".jpg",
                "Descrição curta de " + nomeLari,
                "Uma descrição longa e detalhada de " + nomeLari,
                0,
                new ArrayList<>(), // capturas
                new ArrayList<>(), // curtidas
                new ArrayList<>(), // tags
                new ArrayList<>(), // requisitos
                new ArrayList<>()  // Avaliações
        );
        repository.saveJogo(jogo);

        List<String> tagsLista = common.extrairLista(tags);

        jogoServico.addTagsToGame(jogo.getId(), autorAtual.getUserId(), tagsLista);

        userServico.registerUser(emailJp, senhaJp, nomeJp);
        autorAtual = repository.getByEmail(emailJp);

        jogoServico.addTagsToGame(jogo.getId(), autorAtual.getUserId(), tagsLista);
    }

    @Then("as tags empatadas são exibidas em ordem alfabética")
    public void asTagsEmpatadasSaoExibidasEmOrdemAlfabetica() {
        List<String> esperado = List.of("Ação", "RPG");

        var topTags = jogoServico.getTopTags(jogo.getId());

        List<String> nomesNaOrdem = topTags.stream()
                .map(Tag::getNome)
                .toList();

        assertEquals(esperado, nomesNaOrdem);
    }


    @Given("que o jogo {string} possui as tags {string}")
    public void queOJogoPossuiAsTags(String nomeJogo, String tags) {
        userServico.registerUser(emailJp, senhaJp, nomeJp);
        User jp = repository.getByEmail(emailJp);

        JogoId jogoId = new JogoId(1);
        jogo = new Jogo(
                jogoId,
                nomeJogo,
                "DevStudio",
                "https://example.com/" + nomeJp.toLowerCase() + ".jpg",
                "Descrição curta de " + nomeJp,
                "Uma descrição longa e detalhada de " + nomeJp,
                0,
                new ArrayList<>(), // capturas
                new ArrayList<>(), // curtidas
                new ArrayList<>(), // tags
                new ArrayList<>(), // requisitos
                new ArrayList<>()  // Avaliações
        );
        repository.saveJogo(jogo);

        List<String> tagsIniciais = common.extrairLista(tags);

        jogoServico.addTagsToGame(jogo.getId(), jp.getUserId(), tagsIniciais);

        tagsAntes = jogoServico.getTopTags(jogo.getId());
    }

    @When("o usuário {string} adiciona a tag {string} ao jogo")
    public void oUsuárioAdicionaATagAoJogo(String nome, String tag) {
        userServico.registerUser(emailLari, senhaLari, nome);
        User lari = repository.getByEmail(emailLari);

        List<String> tagLista = common.extrairLista(tag);

        jogoServico.addTagsToGame(jogo.getId(), lari.getUserId(), tagLista);
    }

    @Then("o sistema recalcula e exibe automaticamente as {int} tags mais populares")
    public void oSistemaRecalculaEExibeAutomaticamenteAsTagsMaisPopulares(int arg0) {
        var topTags = jogoServico.getTopTags(jogo.getId());

        assertNotEquals(topTags, tagsAntes);
    }

    @When("o usuário {string} remove a tag {string} do jogo")
    public void oUsuárioRemoveATagDoJogo(String nome, String tag) {
        User jp = repository.getByEmail(emailJp);

        jogoServico.removeTagFromGame(jogo.getId(), tag, jp.getUserId());
    }
}
