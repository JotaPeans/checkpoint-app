package org.checkpoint.dominio.user;

import org.checkpoint.dominio.diario.DiarioId;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.lista.ListaId;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class User {
    private final UserId id;

    private String email;
    private String nome;
    private String senha;
    private String avatarUrl;
    private String bio;
    private boolean isPrivate;
    private boolean emailVerificado;
    private DiarioId diarioId;

    private List<UserId> solicitacoesPendentes;
    private List<RedeSocial> redesSociais;
    private List<UserId> seguindo;
    private List<UserId> seguidores;
    private List<ListaId> listas;
    private List<JogoId> jogosFavoritos;

    public User(String nome,
                String email,
                String senha) {
        this.id = null;
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setAvatarUrl("");
        setBio("");
        setIsPrivate(false);
        setEmailVerificado(false);
    }

    public User(UserId id, String nome,
                String avatarUrl, boolean isPrivate, boolean emailVerificado) {
        this.id = id;
        setNome(nome);
        setSenha("");
        setAvatarUrl(avatarUrl);
        setBio("");
        setIsPrivate(isPrivate);
        setEmailVerificado(emailVerificado);
    }

    public User(
            UserId id,
            String nome,
            String email,
            String senha,
            String avatarUrl,
            String bio,
            boolean isPrivate,
            boolean emailVerificado,
            DiarioId diarioId,
            List<UserId> solicitacoesPendentes,
            List<RedeSocial> redesSociais,
            List<UserId> seguindo,
            List<UserId> seguidores,
            List<ListaId> listas,
            List<JogoId> jogosFavoritos) {
        notNull(id, "O id não pode ser nulo");
        notNull(nome, "O nome não pode ser nulo");
        notNull(email, "O email não pode ser nulo");
        notNull(senha, "A senha não pode ser nula");
        notNull(solicitacoesPendentes, "As solicitacoes pendentes não pode ser nula");
        notNull(redesSociais, "As redes sociais não pode ser nula");
        notNull(seguindo, "A lista de seguindo não pode ser nula");
        notNull(seguidores, "A lista de seguidores não pode ser nula");
        notNull(listas, "As listas de jogos não pode ser nula");
        notNull(jogosFavoritos, "A lista de jogos favoritos não pode ser nula");

        this.id = id;

        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setAvatarUrl(avatarUrl);
        setBio(bio);
        setIsPrivate(isPrivate);
        setEmailVerificado(emailVerificado);
        setSolicitacoesPendentes(solicitacoesPendentes);
        setRedesSociais(redesSociais);
        setSeguindo(seguindo);
        setSeguidores(seguidores);
        setListas(listas);
        setDiarioId(diarioId);
        setJogosFavoritos(jogosFavoritos);
    }

    public UserId getUserId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isEmailVerificado() {
        return emailVerificado;
    }

    public void setEmailVerificado(boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }

    public List<UserId> getSolicitacoesPendentes() {
        return solicitacoesPendentes;
    }

    public void setSolicitacoesPendentes(List<UserId> solicitacoesPendentes) {
        this.solicitacoesPendentes = solicitacoesPendentes;
    }

    public List<RedeSocial> getRedesSociais() {
        return redesSociais;
    }

    public void setRedesSociais(List<RedeSocial> redesSociais) {
        this.redesSociais = redesSociais;
    }

    public List<UserId> getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(List<UserId> seguindo) {
        this.seguindo = seguindo;
    }

    public List<UserId> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<UserId> seguidores) {
        this.seguidores = seguidores;
    }

    public List<ListaId> getListas() {
        return listas;
    }

    public void setListas(List<ListaId> listas) {
        this.listas = listas;
    }

    public DiarioId getDiarioId() {
        return diarioId;
    }

    public void setDiarioId(DiarioId diarioId) {
        this.diarioId = diarioId;
    }

    public List<JogoId> getJogosFavoritos() {
        return jogosFavoritos;
    }

    public void setJogosFavoritos(List<JogoId> jogosFavoritos) {
        this.jogosFavoritos = jogosFavoritos;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
