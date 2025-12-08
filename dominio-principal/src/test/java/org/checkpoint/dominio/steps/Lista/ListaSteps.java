package org.checkpoint.dominio.steps.Lista;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.lista.ListaId;
import org.checkpoint.dominio.lista.ListaJogos;
import org.checkpoint.dominio.steps.Common;
import org.checkpoint.dominio.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListaSteps extends CheckpointFuncionalidade {

    Common common = new Common();

    private String nomeLari = "Lari";
    private String senhaLari = "Larisenha2%";
    private String emailLari = "lss2@cesar.school";
    private String nomeDavi = "Davi";
    private String senhaDavi = "Davisenha2%";
    private String emailDavi = "druy@cesar.school";
    private String notification;

    User lari;
    User davi;
    JogoId jogo1Id;
    JogoId jogo2Id;
    private ListaId listaIdDuplicada;
    private String novoTituloDesejado;
    private JogoId terrariaId;

    private List<Jogo> jogosDaLista;

    @Given("que o usuário {string} cria a lista {string} com o jogo {string} e {string} e modo {string}")
    public void queOUsuárioCriaAListaComOJogoEEModo(String nome, String listaNome, String jogo1Nome, String jogo2Nome, String modo) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        jogo1Id = new JogoId(1);
        jogo2Id = new JogoId(2);

        Jogo jogo1 = new Jogo(
                jogo1Id,
                jogo1Nome,
                "DevStudio",
                "https://example.com/" + jogo1Nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo1Nome,
                "Uma descrição longa e detalhada de " + jogo1Nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        Jogo jogo2 = new Jogo(
                jogo2Id,
                jogo2Nome,
                "DevStudio",
                "https://example.com/" + jogo2Nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo2Nome,
                "Uma descrição longa e detalhada de " + jogo2Nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        repository.saveJogo(jogo1);
        repository.saveJogo(jogo2);

        listaServico.createLista(lari, listaNome, modo.equalsIgnoreCase("privado"));
    }

    @Then("o sistema deve salvar a lista")
    public void oSistemaDeveSalvarALista() {
        ListaJogos listaSalva = repository.getList(new ListaId(1));

        assertEquals("Lari", repository.getUser(listaSalva.getDonoId()).getNome());
        assertTrue(listaSalva.getJogos().isEmpty());
    }

    @Given("que o usuário {string} tenta criar a lista {string} com os jogos {string}")
    public void queOUsuárioTentaCriarAListaComOsJogos(String nome, String listaNome, String jogos) {
        try {
            userServico.registerUser(emailLari, senhaLari, nome);
            lari = repository.getByEmail(emailLari);

            List<String> listaDeJogos = common.extrairLista(jogos);
            jogosDaLista = common.criarJogos(listaDeJogos, repository);

            listaServico.createLista(lari, listaNome, false);

            ListaJogos listaCriada = repository.getListByTituloAndDono(listaNome, lari.getUserId());
            assertNotNull(listaCriada, "A lista criada não foi encontrada no repositório.");

            List<JogoId> jogoIds = jogosDaLista.stream()
                    .map(Jogo::getId)
                    .toList();

            listaServico.updateJogos(lari, listaCriada.getId(), jogoIds);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema notifica que Uma lista não pode conter mais de {int} jogos")
    public void oSistemaNotificaQueUmaListaNãoPodeConterMaisDeJogos(int arg0) {
        assertEquals("Uma lista não pode conter mais de 100 jogos", notification);
    }

    @Given("que o usuário {string} duplica a lista {string} do usuario {string}")
    public void queOUsuárioDuplicaAListaDoUsuario(String nome1, String nomeLista, String nome2) {
        userServico.registerUser(emailLari, senhaLari, nome1);
        lari = repository.getByEmail(emailLari);

        userServico.registerUser(emailDavi, senhaDavi, nome2);
        davi = repository.getByEmail(emailDavi);

        listaServico.createLista(davi, nomeLista, false);

        ListaJogos listaOrigem = repository.getListByTituloAndDono(nomeLista, davi.getUserId());

        listaServico.duplicateLista(lari, listaOrigem.getId());
    }

    @Then("a lista {string} aparece nas listas do usuário")
    public void aListaApareceNasListasDoUsuário(String nomeListaEsperada) {
        List<ListaId> idsListas = lari.getListas();

        List<ListaJogos> listasDoUsuario = idsListas.stream()
                .map(repository::getList)
                .filter(lista -> lista != null)
                .toList();

        boolean encontrada = listasDoUsuario.stream()
                .anyMatch(lista -> lista.getTitulo().equalsIgnoreCase(nomeListaEsperada));
    }
    @Given("que o usuário {string} duplica a sua lista {string}")
    public void queOUsuarioDuplicaASuaLista(String nome, String nomeLista) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        listaServico.createLista(lari, nomeLista, true);

        ListaJogos listaOrigem = repository.getListByTituloAndDono(nomeLista, lari.getUserId());
        assertNotNull(listaOrigem, "Lista de origem não encontrada.");

        listaServico.duplicateLista(lari, listaOrigem.getId());
    }

    @Given("que o usuário {string} tem a lista duplicada {string}")
    public void queOUsuarioTemAListaDuplicada(String nome, String tituloDuplicado) {

        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        listaServico.createLista(lari, tituloDuplicado, false);

        ListaJogos lista = repository.getListByTituloAndDono(tituloDuplicado, lari.getUserId());
        assertNotNull(lista, "Lista duplicada não foi criada/encontrada.");
        listaIdDuplicada = lista.getId();
    }

    @When("o nome é editado para {string} e o jogo {string} é adicionado")
    public void oNomeEEditadoEJogoAdicionado(String novoTitulo, String nomeJogo) {
        novoTituloDesejado = novoTitulo;

        terrariaId = new JogoId(1001);
        Jogo terraria = new Jogo(
                terrariaId,
                nomeJogo,
                "DevStudio",
                "https://example.com/" + nomeJogo.toLowerCase().replace(" ", "-") + ".jpg",
                "Descrição curta de " + nomeJogo,
                "Uma descrição longa e detalhada de " + nomeJogo,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        repository.saveJogo(terraria);

        listaServico.updateTitulo(lari, listaIdDuplicada, novoTitulo);

        List<JogoId> novos = new ArrayList<>();
        novos.add(terrariaId);
        listaServico.updateJogos(lari, listaIdDuplicada, novos);
    }

    @Then("a lista é atualizada")
    public void aListaEAtualizada() {
        ListaJogos atual = repository.getList(listaIdDuplicada);
        assertNotNull(atual, "Lista não encontrada após edição.");

        assertEquals(novoTituloDesejado, atual.getTitulo(), "Título não foi atualizado.");

        assertTrue(atual.getJogos().contains(terrariaId), "O jogo não foi adicionado à lista.");
    }
}