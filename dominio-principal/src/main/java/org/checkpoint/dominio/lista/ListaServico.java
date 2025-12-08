package org.checkpoint.dominio.lista;

import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.dominio.user.UserRepositorio;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class ListaServico {
    private final ListaJogosRepositorio listaJogosRepositorio;
    private final UserRepositorio userRepositorio;

    public ListaServico(ListaJogosRepositorio listaJogosRepositorio, UserRepositorio userRepositorio) {
        notNull(listaJogosRepositorio, "O repositório de listas não pode ser nulo");
        notNull(userRepositorio, "O repositório de usuários não pode ser nulo");
        this.listaJogosRepositorio = listaJogosRepositorio;
        this.userRepositorio = userRepositorio;
    }

    public ListaJogos getListaById(User dono, ListaId listaId) {
        ListaJogos lista = this.listaJogosRepositorio.getList(listaId);

        notNull(lista, "Lista não encontrada");

        if(lista.getIsPrivate() && lista.getDonoId().equals(dono.getUserId())) {
            return lista;
        }
        else if(!lista.getIsPrivate()) {
            return lista;
        }

        throw new IllegalArgumentException("Lista não encontrada");
    }

    public List<ListaJogos> listListasByUser(User dono) {
        return this.listaJogosRepositorio.getListByDono(dono.getUserId());
    }

    public List<ListaJogos> getListasPublicas(User user) {
        return this.listaJogosRepositorio.getPublicLists(user.getUserId());
    }

    // =====================
    // Criação e duplicação
    // =====================
    public void createLista(User dono, String titulo, boolean isPrivate) {
        notNull(dono, "O dono da lista não pode ser nulo");
        notNull(titulo, "O título da lista não pode ser nulo");

        ListaJogos criada = this.listaJogosRepositorio.createList(dono.getUserId(), titulo, isPrivate);

        List<ListaId> listas = dono.getListas();
        if (listas == null) listas = new ArrayList<>();
        listas.add(criada.getId());
        dono.setListas(listas);
        userRepositorio.saveUser(dono);
    }

    public void duplicateLista(User novoDono, ListaId listaOrigemId) {
        notNull(novoDono, "O dono da nova lista não pode ser nulo");
        notNull(listaOrigemId, "O id da lista de origem não pode ser nulo");

        ListaJogos origem = this.listaJogosRepositorio.getList(listaOrigemId);
        notNull(origem, "Lista de origem não encontrada");

        if(origem.getIsPrivate() && !origem.getDonoId().equals(novoDono.getUserId())) {
            throw new IllegalArgumentException("Lista não encontrada");
        }

        String novoTitulo = "CÓPIA " + origem.getTitulo();

        ListaJogos copia = this.listaJogosRepositorio.createList(
                novoDono.getUserId(),
                novoTitulo,
                origem.getIsPrivate()
        );

        copia.setJogos(new ArrayList<>(origem.getJogos()));
        this.listaJogosRepositorio.saveList(copia);

        List<ListaId> listasDoUsuario = novoDono.getListas();
        if (listasDoUsuario == null) listasDoUsuario = new ArrayList<>();
        listasDoUsuario.add(copia.getId());
        novoDono.setListas(listasDoUsuario);

        userRepositorio.saveUser(novoDono);
    }

    public void deleteLista(User user, ListaId listaId) {
        notNull(user, "O usuario não pode ser nulo");
        notNull(listaId, "O id da lista não pode ser nulo");

        ListaJogos listaJogos = this.listaJogosRepositorio.getList(listaId);
        notNull(listaJogos, "Lista não encontrada");

        isTrue(listaJogos.getDonoId().equals(user.getUserId()), "Lista não encontrada");

        this.listaJogosRepositorio.deleteList(listaId);
    }

    // =====================
    // Atualização
    // =====================
    public void updateTitulo(User user, ListaId listaId, String novoTitulo) {
        notNull(user, "O usuario não pode ser nulo");
        notNull(listaId, "O id da lista não pode ser nulo");
        notNull(novoTitulo, "O novo título não pode ser nulo");

        ListaJogos listaJogos = this.listaJogosRepositorio.getList(listaId);
        notNull(listaJogos, "Lista não encontrada");

        boolean isUserOwnerOfList = listaJogos.getDonoId().getId() == user.getUserId().getId();
        isTrue(isUserOwnerOfList, "O usuário não é dono da lista");

        listaJogos.setTitulo(novoTitulo);
        this.listaJogosRepositorio.saveList(listaJogos);
    }

    public void updateJogos(User user, ListaId listaId, List<JogoId> novosJogos) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(listaId, "O id da lista não pode ser nulo");
        notNull(novosJogos, "A nova lista de jogos não pode ser nula");

        ListaJogos listaJogos = this.listaJogosRepositorio.getList(listaId);
        notNull(listaJogos, "Lista não encontrada");

        boolean isUserOwnerOfList = listaJogos.getDonoId().getId() == user.getUserId().getId();
        isTrue(isUserOwnerOfList, "O usuário não é dono da lista");

        List<JogoId> jogosLista = new ArrayList<>(listaJogos.getJogos());

        // valida limite (antes de adicionar)
        int totalJogos = jogosLista.size() + novosJogos.size();
        isTrue(totalJogos <= 100, "Uma lista não pode conter mais de 100 jogos");

        listaJogos.setJogos(novosJogos);
        this.listaJogosRepositorio.saveList(listaJogos);
    }

    public void togglePrivacidade(User user, ListaId listaId, Boolean isPrivate) {
        notNull(user, "O usuario não pode ser nulo");
        notNull(listaId, "O id da lista não pode ser nulo");
        notNull(isPrivate, "O parâmetro 'publica' não pode ser nulo");

        ListaJogos listaJogos = this.listaJogosRepositorio.getList(listaId);
        notNull(listaJogos, "Lista não encontrada");

        listaJogos.setIsPrivate(isPrivate);
        this.listaJogosRepositorio.saveList(listaJogos);
    }

    // =====================
    // Curtidas
    // =====================
    public void toggleListaLike(User user, ListaId listaId) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(listaId, "O id da lista não pode ser nulo");

        ListaJogos listaJogos = this.listaJogosRepositorio.getList(listaId);
        notNull(listaJogos, "Lista não encontrada");

        List<UserId> curtidas = listaJogos.getCurtidas();
        if (curtidas.contains(user.getUserId())) {
            curtidas.remove(user.getUserId());
        } else {
            curtidas.add(user.getUserId());
        }

        listaJogos.setCurtidas(curtidas);
        this.listaJogosRepositorio.saveList(listaJogos);
    }
}