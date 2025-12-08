package org.checkpoint.dominio.steps.Diario;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.diario.*;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DiarioSteps extends CheckpointFuncionalidade {

    private final String nomeLari  = "Lari";
    private final String senhaLari = "Larisenha2%";
    private final String emailLari = "lss2@cesar.school";
    private  String notification;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private User lari;
    private JogoId jogoId;
    private RegistroId registroId;

    private Date dataInicioInformada;
    private Date dataTerminoInformada;
    double percentualCalculado;

    @Given("que o usuário {string} inicia o registro do jogo {string}")
    public void queOUsuárioIniciaORegistroDoJogo(String nome, String jogoNome) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        DiarioId diarioId = new DiarioId(1);
        Diario diario = new Diario(diarioId, lari.getUserId(), new ArrayList<>());
        repository.saveDiario(diario);
        lari.setDiarioId(diarioId);
        repository.saveUser(lari);

        jogoId = new JogoId(1);
        Jogo jogo = new Jogo(
                jogoId,
                jogoNome,
                "DevStudio",
                "https://example.com/" + jogoNome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogoNome,
                "Uma descrição longa e detalhada de " + jogoNome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        repository.saveJogo(jogo);
    }

    @When("ele informa a data de início {string} e a data de término {string}")
    public void eleInformaADataDeInícioEADataDeTérmino(String dataInicioStr, String dataTerminoStr) {
        try {
            dataInicioInformada  = sdf.parse(dataInicioStr);
            dataTerminoInformada = sdf.parse(dataTerminoStr);

            diarioServico.addRegistro(lari, jogoId, dataInicioInformada, dataTerminoInformada);

        } catch (ParseException | IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema deve salvar o registro")
    public void oSistemaDeveSalvarORegistro() {
        Diario diario = repository.getDiario(lari.getDiarioId());

        List<RegistroDiario> registros = diario.getRegistros();
        assertFalse(registros.isEmpty());

        RegistroId ultimoId = registros.getLast().getId();

        RegistroDiario registro = repository.getRegistroDiario(ultimoId);

        assertEquals(jogoId, registro.getJogoId());
        assertEquals(dataInicioInformada, registro.getDataInicio());
        assertEquals(dataTerminoInformada, registro.getDataTermino());
    }

    @Then("o sistema notifica que A data de término não pode ser anterior à de início")
    public void oSistemaNotificaQueADataDeTérminoNãoPodeSerAnteriorÀDeInício() {
        assertEquals(notification, "A data de término não pode ser anterior à de início");
    }

    @When("ele informa apenas a data de início {string}")
    public void eleInformaApenasADataDeInício(String dataInicioStr) {
        try {
            dataInicioInformada  = sdf.parse(dataInicioStr);

            diarioServico.addRegistro(lari, jogoId, dataInicioInformada, null);

        } catch (ParseException | IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @When("ele informa a data de término {string}")
    public void eleInformaADataDeTérmino(String dataTerminoStr) {
        try {
            dataTerminoInformada  = sdf.parse(dataTerminoStr);

            diarioServico.addRegistro(lari, jogoId, null, dataTerminoInformada);

        } catch (ParseException | IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema notifica que A data de início não pode ser nula")
    public void oSistemaNotificaQueADataDeInícioNãoPodeSerNula() {
        assertEquals(notification, "A data de início não pode ser nula");
    }

    @Given("que o usuário {string} acessa o diário")
    public void queOUsuárioAcessaODiário(String nome) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        DiarioId diarioId = new DiarioId(1);
        Diario diario = new Diario(diarioId, lari.getUserId(), new ArrayList<>());
        repository.saveDiario(diario);
        lari.setDiarioId(diarioId);
        repository.saveUser(lari);
    }

    @When("ele registra a conquista {string} com data {string}, status {string} e o jogo  {string}")
    public void eleRegistraAConquistaComDataStatusEOJogo(String nomeConquista, String data, String status, String jogoNome) throws ParseException {
        jogoId = new JogoId(1);
        Jogo jogo = new Jogo(
                jogoId,
                jogoNome,
                "DevStudio",
                "https://example.com/" + jogoNome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogoNome,
                "Uma descrição longa e detalhada de " + jogoNome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        repository.saveJogo(jogo);

        Date dataInformada  = sdf.parse(data);

        diarioServico.addRegistro(lari, jogoId, dataInformada, null);

        Diario diario = repository.getDiarioByDono(lari);
        List<RegistroDiario> registro = diario.getRegistros();

        diarioServico.addConquista(registro.getFirst().getId(), nomeConquista, dataInformada, true);
    }

    @Then("o sistema deve salvar a conquista vinculada ao jogo")
    public void oSistemaDeveSalvarAConquistaVinculadaAoJogo() {
        Diario diario = repository.getDiarioByDono(lari);

        List<RegistroDiario> registros = diario.getRegistros();

        RegistroId registroId = registros.getLast().getId();
        RegistroDiario registro = repository.getRegistroDiario(registroId);
        assertNotNull(registro);

        List<Conquista> conquistasIds = registro.getConquistas();
        assertFalse(conquistasIds.isEmpty(), "Nenhuma conquista foi salva no registro.");

        ConquistaId conquistaId = conquistasIds.getLast().getId();
        Conquista conquista = repository.getConquista(conquistaId);
        assertNotNull(conquista);
    }

    @When("ele registra uma conquista com data {string}, status {string} e o jogo {string}")
    public void eleRegistraUmaConquistaComDataStatusEOJogo(String data, String status, String jogoNome) throws ParseException {
        jogoId = new JogoId(1);
        Jogo jogo = new Jogo(
                jogoId,
                jogoNome,
                "DevStudio",
                "https://example.com/" + jogoNome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogoNome,
                "Uma descrição longa e detalhada de " + jogoNome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        repository.saveJogo(jogo);

        Date dataInformada  = sdf.parse(data);

        diarioServico.addRegistro(lari, jogoId, dataInformada, null);

        Diario diario = repository.getDiarioByDono(lari);
        List<RegistroDiario> registro = diario.getRegistros();

        try {
            diarioServico.addConquista(registro.getFirst().getId(), null, dataInformada, true);
        }catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema notifica que O nome da conquista não pode ser nulo")
    public void oSistemaNotificaQueONomeDaConquistaNãoPodeSerNulo() {
        assertEquals(notification, "O nome da conquista não pode ser nulo");
    }

    @When("ele registra a conquista {string}, status {string} e o jogo {string}")
    public void eleRegistraAConquistaStatusEOJogo(String nomeConquista, String status, String jogoNome) throws ParseException {
        jogoId = new JogoId(1);
        Jogo jogo = new Jogo(
                jogoId,
                jogoNome,
                "DevStudio",
                "https://example.com/" + jogoNome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogoNome,
                "Uma descrição longa e detalhada de " + jogoNome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        repository.saveJogo(jogo);

        Date dataInformada  = sdf.parse("22/10/25");

        diarioServico.addRegistro(lari, jogoId, dataInformada, null);

        Diario diario = repository.getDiarioByDono(lari);
        List<RegistroDiario> registro = diario.getRegistros();

        try {
            diarioServico.addConquista(registro.getFirst().getId(), nomeConquista, null, true);
        }catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema notifica que A data de desbloqueio não pode ser nula")
    public void oSistemaNotificaQueADataDeDesbloqueioNãoPodeSerNula() {
        assertEquals(notification, "A data de desbloqueio não pode ser nula");
    }

    @Given("que o usuário {string} já registrou a conquista {string} no jogo {string}")
    public void queOUsuárioJáRegistrouAConquistaNoJogo(String nome, String nomeConquista, String jogoNome) throws ParseException {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        DiarioId diarioId = new DiarioId(1);
        Diario diario = new Diario(diarioId, lari.getUserId(), new ArrayList<>());
        repository.saveDiario(diario);
        lari.setDiarioId(diarioId);
        repository.saveUser(lari);

        jogoId = new JogoId(1);
        Jogo jogo = new Jogo(
                jogoId,
                jogoNome,
                "DevStudio",
                "https://example.com/" + jogoNome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogoNome,
                "Uma descrição longa e detalhada de " + jogoNome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        repository.saveJogo(jogo);

        Date dataInformada  = sdf.parse("25/10/25");

        diarioServico.addRegistro(lari, jogoId, dataInformada, null);

        List<RegistroDiario> registro = diario.getRegistros();

        diarioServico.addConquista(registro.getFirst().getId(), nomeConquista, dataInformada, true);
    }
    @When("ele tenta registrar novamente a conquista {string} para o mesmo jogo")
    public void eleTentaRegistrarNovamenteAConquistaParaOMesmoJogo(String nomeConquista) {
        try {
            Diario diario = repository.getDiarioByDono(lari);
            List<RegistroDiario> registros = diario.getRegistros();
            RegistroId registroId = registros.getFirst().getId();

            Date data = sdf.parse("25/10/25");
            diarioServico.addConquista(registroId, nomeConquista, data, true);
        } catch (Exception e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema notifica que A conquista já foi registrada")
    public void oSistemaNotificaQueAConquistaJáFoiRegistrada() {
        assertEquals("A conquista já foi registrada", notification);
    }

    @Given("que o usuário {string} registrou {int} conquistas como {string} no jogo {string}")
    public void queOUsuárioRegistrouConquistasComoNoJogo(String nome, Integer quantidade, String status, String jogoNome) throws ParseException {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        DiarioId diarioId = new DiarioId(1);
        Diario diario = new Diario(diarioId, lari.getUserId(), new ArrayList<>());
        repository.saveDiario(diario);
        lari.setDiarioId(diarioId);
        repository.saveUser(lari);

        jogoId = new JogoId(1);
        Jogo jogo = new Jogo(
                jogoId,
                jogoNome,
                "DevStudio",
                "https://example.com/" + jogoNome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogoNome,
                "Uma descrição longa e detalhada de " + jogoNome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        repository.saveJogo(jogo);

        Date dataInformada = sdf.parse("10/10/2025");
        diarioServico.addRegistro(lari, jogoId, dataInformada, null);

        Diario diarioRecuperado = repository.getDiarioByDono(lari);
        RegistroId registroId = diarioRecuperado.getRegistros().getFirst().getId();

        boolean concluida = status.equalsIgnoreCase("concluídas");
        for (int i = 1; i <= quantidade; i++) {
            diarioServico.addConquista(registroId, "Conquista " + i, dataInformada, concluida);
        }
    }

    @When("o usuário registra {int} conquistas como {string} no jogo")
    public void oUsuárioRegistraConquistasComoNoJogo(Integer quantidade, String status) throws ParseException {
        Diario diarioRecuperado = repository.getDiarioByDono(lari);
        RegistroId registroId = diarioRecuperado.getRegistros().getFirst().getId();

        boolean concluida = status.equalsIgnoreCase("concluídas");
        Date dataInformada = sdf.parse("15/10/2025");

        int inicio = 12;
        for (int i = inicio; i < inicio + quantidade; i++) {
            diarioServico.addConquista(registroId, "Conquista " + i, dataInformada, concluida);
        }

        percentualCalculado = diarioServico.getPercentualConquistas(registroId);
    }

    @Then("o sistema deve calcular e mostrar que {string} das conquistas foram concluídas")
    public void oSistemaDeveCalcularEMostrarQueDasConquistasForamConcluídas(String percentualEsperado) {
        String percentualFormatado = String.format("%.2f%%", percentualCalculado);
        assertEquals(percentualEsperado, percentualFormatado);
    }

}
