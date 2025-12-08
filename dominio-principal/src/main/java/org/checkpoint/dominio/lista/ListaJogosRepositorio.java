package org.checkpoint.dominio.lista;

import org.checkpoint.dominio.user.UserId;

import java.util.List;

public interface ListaJogosRepositorio {
    void saveList(ListaJogos lista);

    ListaJogos createList(UserId donoId, String titulo, boolean isPrivate);

    ListaJogos getList(ListaId id);

    List<ListaJogos> getPublicLists(UserId userId);

    public ListaJogos getListByTituloAndDono(String titulo, UserId donoId);
    public List<ListaJogos> getListByDono(UserId donoId);

    void deleteList(ListaId id);
}
