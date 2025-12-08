package org.checkpoint.persistencia.jpa.lista;

import org.checkpoint.dominio.lista.ListaId;
import org.checkpoint.dominio.lista.ListaJogos;
import org.checkpoint.dominio.lista.ListaJogosRepositorio;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.persistencia.jpa.JpaMapeador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ListaRepositorioImpl implements ListaJogosRepositorio {

    private @Autowired ListaJpaRepositorio repositorio;

    private @Autowired JpaMapeador mapeador;

    @Override
    public void saveList(ListaJogos lista) {
        var listaJpa = mapeador.map(lista, ListaJpa.class);

        repositorio.save(listaJpa);
    }

    @Override
    public ListaJogos createList(UserId donoId, String titulo, boolean isPrivate) {
        ListaJogos lista = new ListaJogos(donoId, titulo, isPrivate);

        var listaJpa = mapeador.map(lista, ListaJpa.class);

        var savedLista = repositorio.save(listaJpa);

        return mapeador.map(savedLista, ListaJogos.class);
    }

    @Override
    public ListaJogos getList(ListaId id) {
        Optional<ListaJpa> listaJpa = repositorio.findById(id.getId());

        if (listaJpa.isEmpty()) {
            return null;
        }

        return mapeador.map(repositorio.findById(id.getId()), ListaJogos.class);
    }

    @Override
    public List<ListaJogos> getPublicLists(UserId userId) {
        List<ListaJpa> listasJpa = this.repositorio.findAllByIsPrivateFalseAndDono_IdNot(userId.getId());

        List<ListaJogos> listas = new ArrayList<>();

        for (ListaJpa listaJpa : listasJpa) {
            listas.add(mapeador.map(listaJpa, ListaJogos.class));
        }

        return listas;
    }

    @Override
    public ListaJogos getListByTituloAndDono(String titulo, UserId donoId) {
        Optional<ListaJpa> listaJpa = repositorio.findByTituloAndDono_Id(titulo, donoId.getId());

        if (listaJpa.isEmpty()) {
            return null;
        }

        return mapeador.map(listaJpa, ListaJogos.class);
    }

    @Override
    public List<ListaJogos> getListByDono(UserId donoId) {
        List<ListaJpa> listasJpa = repositorio.findByDono_Id(donoId.getId());

        List<ListaJogos> listas = new ArrayList<>();

        for (ListaJpa listajpa : listasJpa) {
            listas.add(mapeador.map(listajpa, ListaJogos.class));
        }

        return listas;
    }

    @Override
    public void deleteList(ListaId listaId) {
        Optional<ListaJpa> listaJpa = repositorio.findById(listaId.getId());

        if (listaJpa.isEmpty()) {
            return;
        }

        this.repositorio.delete(listaJpa.get());
    }
}
