package org.checkpoint.dominio.user;

import org.checkpoint.dominio.autenticacao.Autenticacao;
import org.checkpoint.dominio.diario.Diario;
import org.checkpoint.dominio.diario.DiarioRepositorio;
import org.checkpoint.dominio.email.EmailSenderService;
import org.checkpoint.dominio.email.Token;
import org.checkpoint.dominio.email.VerificacaoEmail;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.observer.FollowEventObserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class UserServico {
    private final UserRepositorio userRepositorio;
    private final DiarioRepositorio diarioRepositorio;
    private final EmailSenderService emailSenderService;
    private final Autenticacao autenticacao;

    // Lista de observers para eventos de seguir
    private final List<FollowEventObserver> observadores;

    public UserServico(UserRepositorio userRepositorio, DiarioRepositorio diarioRepositorio,
                       EmailSenderService emailSenderService, Autenticacao autenticacao) {
        notNull(userRepositorio, "O repositório de usuários não pode ser nulo");
        notNull(diarioRepositorio, "O repositório de diario não pode ser nulo");
        notNull(emailSenderService, "O servico de email não pode ser nulo");
        notNull(autenticacao, "A implementação de autententicacao não pode ser nula");

        this.userRepositorio = userRepositorio;
        this.diarioRepositorio = diarioRepositorio;
        this.emailSenderService = emailSenderService;
        this.autenticacao = autenticacao;
        this.observadores = new ArrayList<>();
    }

    public void adicionarObservador(FollowEventObserver observador) {
        observadores.add(observador);
    }

    public void removerObservador(FollowEventObserver observador) {
        observadores.remove(observador);
    }

    public void solicitarSeguir(User solicitante, User alvo) {
        for (FollowEventObserver observador : observadores) {
            observador.onSolicitacaoParaSeguir(solicitante, alvo);
        }
    }

    public String login(String email, String senha) {
        notNull(email, "O email não pode ser nulo");
        notNull(senha, "A senha não pode ser nula");

        return this.autenticacao.login(email, senha);
    }

    public String registerUser(String email, String senha, String nome) {
        notNull(email, "O email não pode ser nulo");
        notNull(senha, "A senha não pode ser nula");
        notNull(nome, "O nome não pode ser nulo");

        // lógica de registro
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

        boolean emailValido = EMAIL_PATTERN.matcher(email).matches();
        isTrue(emailValido, "O e-mail está fora do padrão");

        String PASSWORD_REGEX = "^(?=.*[a-z])" + // pelo menos uma letra minúscula
                "(?=.*[A-Z])" + // pelo menos uma letra maiúscula
                "(?=.*\\d)" + // pelo menos um número
                "(?=.*[@$!%*?&^#()\\[\\]{}._+\\-=<>])" + // pelo menos um caractere especial
                ".{8,}$"; // pelo menos 8 caracteres

        Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        boolean senhaValida = PASSWORD_PATTERN.matcher(senha).matches();
        isTrue(
                senhaValida,
                "A senha deve conter, no mínimo, " +
                        "pelo menos uma letra minúscula, pelo menos uma letra maiúscula, " +
                        "pelo menos um número, pelo menos um caracter especial, e pelo menos 8 digitos");

        boolean isEmailAlreadyInUse = this.isEmailAlreadyInUse(email);
        isTrue(!isEmailAlreadyInUse, "O e-mail já está cadastrado");

        User user = userRepositorio.createUser(email, senha, nome);

        diarioRepositorio.saveDiario(new Diario(user.getUserId()));

        String token = this.emailSenderService.generateVerificationToken(email, user.getUserId());
        this.emailSenderService.sendVerificationEmail(email, token);

        return "O email de verificação com o token foi enviado para o email";
    }

    public void verifyUserEmail(Token token) {
        notNull(token, "O token não pode ser nulo");

        VerificacaoEmail verificacaoEmail = this.emailSenderService.getVerificacaoEmailByToken(token);

        notNull(verificacaoEmail, "Verificação de email não encontrada");

        Date agora = new Date();
        isTrue(verificacaoEmail.getDataExpiracao().after(agora), "O token está expirado");

        User user = this.userRepositorio.getUser(verificacaoEmail.getUserId());

        notNull(user, "Usuário para verificação de email não encontrado");

        user.setEmailVerificado(true);

        this.userRepositorio.saveUser(user);

        this.emailSenderService.deleteVerificacaoEmailByToken(token);
    }

    public User getUserById(UserId userId) {
        notNull(userId, "O userId não pode ser nulo");

        User user = this.userRepositorio.getUser(userId);

        notNull(user, "Usuário não encontrado");

        return user;
    }

    public void addJogoFavorito(User user, Jogo jogo) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(jogo, "O jogo não pode ser nulo");

        List<JogoId> jogosFavoritos = user.getJogosFavoritos();

        if (jogosFavoritos == null) {
            jogosFavoritos = new ArrayList<>();
        }

        if (jogosFavoritos.contains(jogo.getId())) {
            throw new IllegalArgumentException("O jogo já está entre os favoritos");
        }

        if (jogosFavoritos.size() >= 4) {
            throw new IllegalArgumentException("O limite de jogos favoritos é 4");
        }

        jogosFavoritos.add(jogo.getId());

        user.setJogosFavoritos(jogosFavoritos);
        this.userRepositorio.saveUser(user);
    }

    public void removeJogoFavorito(User user, Jogo jogo) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(jogo, "O jogo não pode ser nulo");

        List<JogoId> jogosFavoritos = user.getJogosFavoritos();
        jogosFavoritos.remove(jogo.getId());

        user.setJogosFavoritos(jogosFavoritos);
        this.userRepositorio.saveUser(user);
    }

    public void reorderJogoFavorito(User user, List<JogoId> novaOrdem) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(novaOrdem, "A nova ordem não pode ser nula");

        List<JogoId> favoritosAtuais = user.getJogosFavoritos();

        if (favoritosAtuais == null || favoritosAtuais.isEmpty()) {
            throw new IllegalArgumentException("O usuário não possui jogos favoritos para reordenar");
        }

        if (favoritosAtuais.size() != novaOrdem.size()) {
            throw new IllegalArgumentException("A nova ordem deve conter todos os jogos favoritos atuais");
        }

        if (!favoritosAtuais.containsAll(novaOrdem) || !novaOrdem.containsAll(favoritosAtuais)) {
            throw new IllegalArgumentException("A nova ordem deve conter exatamente os mesmos jogos favoritos");
        }

        user.setJogosFavoritos(new ArrayList<>(novaOrdem));
        this.userRepositorio.saveUser(user);
    }

    public void updateProfile(User user, String nome, String bio, List<RedeSocial> redesSociais) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(nome, "O nome não pode ser nulo");
        notNull(redesSociais, "A lista de redes sociais não pode ser nula");

        notNull(user, "Usuário não encontrado");

        user.setNome(nome);
        user.setBio(bio);
        user.setRedesSociais(redesSociais);
        this.userRepositorio.saveUser(user);
    }

    public void updateAvatar(User user, String avatarUrl) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(avatarUrl, "A URL do avatar não pode ser nula");

        notNull(user, "Usuário não encontrado");

        user.setAvatarUrl(avatarUrl);
        this.userRepositorio.saveUser(user);
    }

    public void togglePrivacidade(User user, Boolean isPrivate) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(isPrivate, "O parâmetro 'isPrivate' não pode ser nulo");

        notNull(user, "Usuário não encontrado");

        user.setIsPrivate(isPrivate);
        this.userRepositorio.saveUser(user);
    }

    // =====================
    // Seguidores
    // =====================
    public void toggleSeguir(User fromUser, UserId userAlvoId) {
        notNull(fromUser, "O seguidor não pode ser nulo");
        notNull(userAlvoId, "O usuário alvo não pode ser nulo");

        User toUser = this.userRepositorio.getUser(userAlvoId);

        notNull(fromUser, "Usuário não encontrado");
        notNull(toUser, "Usuário não encontrado");

        if (toUser.getIsPrivate()) {
            // Usuário é privado -> cria solicitação pendente
            List<UserId> solicitacoesPendentes = toUser.getSolicitacoesPendentes();

            if (!solicitacoesPendentes.contains(fromUser.getUserId())) {
                solicitacoesPendentes.add(fromUser.getUserId());
                toUser.setSolicitacoesPendentes(solicitacoesPendentes);

                // Notifica os observers sobre a solicitação
                solicitarSeguir(fromUser, toUser);
            }
        } else {
            // Usuário é público -> segue diretamente
            List<UserId> fromUserSeguindo = fromUser.getSeguindo();
            List<UserId> toUserSeguidores = toUser.getSeguidores();
        }

        userRepositorio.saveUser(fromUser);
        userRepositorio.saveUser(toUser);
    }

    public void approveSeguidor(User dono, UserId solicitanteId) {
        notNull(dono, "O dono não pode ser nulo");
        notNull(solicitanteId, "O solicitante não pode ser nulo");

        User solicitante = this.userRepositorio.getUser(solicitanteId);

        notNull(dono, "Usuário não encontrado");
        notNull(solicitante, "Usuário não encontrado");

        List<UserId> solicitacoesPendentes = dono.getSolicitacoesPendentes();

        boolean temSolicitacaoPendente = solicitacoesPendentes.contains(solicitante.getUserId());

        isTrue(temSolicitacaoPendente, "Nenhuma solicitação pendente encontrada.");

        solicitacoesPendentes.remove(solicitante.getUserId());
        dono.setSolicitacoesPendentes(solicitacoesPendentes);

        dono.getSeguidores().add(solicitante.getUserId());

        List<UserId> pessoasSeguindoSolicitante = solicitante.getSeguindo();
        pessoasSeguindoSolicitante.add(dono.getUserId());
        solicitante.setSeguindo(pessoasSeguindoSolicitante);

        userRepositorio.saveUser(dono);
        userRepositorio.saveUser(solicitante);
    }

    public void rejectSeguidor(User dono, UserId solicitanteId) {
        notNull(dono, "O dono não pode ser nulo");
        notNull(solicitanteId, "O solicitante não pode ser nulo");

        User solicitante = this.userRepositorio.getUser(solicitanteId);

        notNull(dono, "Usuário não encontrado");
        notNull(solicitante, "Usuário não encontrado");

        List<UserId> solicitacoesPendentes = dono.getSolicitacoesPendentes();

        boolean temSolicitacaoPendente = solicitacoesPendentes.contains(solicitante.getUserId());

        isTrue(temSolicitacaoPendente, "Nenhuma solicitação pendente encontrada.");

        solicitacoesPendentes.remove(solicitante.getUserId());

        userRepositorio.saveUser(dono);
    }

    public User getInformacoes(User solicitante, UserId solicitadoId) {
        notNull(solicitante, "O solicitante não pode ser nulo");
        notNull(solicitadoId, "O usuário solicitado não pode ser nulo");

        User solicitado = userRepositorio.getUser(solicitadoId);

        if (solicitado == null) {
            return null;
        }

        if (!solicitado.getIsPrivate()) {
            return solicitado;
        }

        if (solicitante.getUserId().equals(solicitado.getUserId())) {
            return solicitado;
        }

        if (solicitado.getSeguidores() != null && solicitado.getSeguidores().contains(solicitante.getUserId())) {
            return solicitado;
        }

        return new User(solicitadoId, solicitado.getNome(), solicitado.getAvatarUrl(), solicitado.getIsPrivate(), solicitado.isEmailVerificado());
    }

    // =====================
    // Validações
    // =====================
    public Boolean isEmailAlreadyInUse(String email) {
        notNull(email, "O email não pode ser nulo");
        User user = this.userRepositorio.getByEmail(email);
        return user != null;
    }
}