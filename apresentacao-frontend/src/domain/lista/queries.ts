import { api, apiWrapper, type MessageType } from "@/lib/api";
import type { ListaJogos } from "./ListaJogos";

interface ListaBody {
  titulo: string;
  isPrivate: boolean;
}

interface JogosBody {
  jogosIds: number[];
}

export const getListaById = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.get<ListaJogos>(`/lista/${id}`);
    return data;
  }, "get-lista-by-id");
};

export const listMyListas = async () => {
  return await apiWrapper(async () => {
    const { data } = await api.get<ListaJogos[]>(`/lista`);
    return data;
  }, "list-my-listas");
};

export const listListasByIds = async (ids: (string | number)[]) => {
  return await apiWrapper(async () => {
    return await Promise.all(
      ids.map(async (id) => {
        const { data } = await api.get<ListaJogos>(`/lista/${id}`);
        return data;
      })
    );
  }, "list-my-listas");
};

export const deleteListaById = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.delete<MessageType>(`/lista/${id}`);
    return data;
  }, "delete-lista-by-id");
};

export const getListasPublicas = async () => {
  return await apiWrapper(async () => {
    const { data } = await api.get<ListaJogos[]>(`/lista/list/public`);
    return data;
  }, "get-listas-publicas");
};

export const createLista = async (body: ListaBody) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/lista`, body);
    return data;
  }, "create-lista");
};

export const updateJogosByListaId = async (
  id: number | string,
  body: JogosBody
) => {
  return await apiWrapper(async () => {
    const { data } = await api.patch<MessageType>(
      `/lista/${id}/jogo`,
      body
    );
    return data;
  }, "change-lista-jogos-by-lista-id");
};

export const changePrivacidadeByListaId = async (
  id: number | string,
  body: Omit<ListaBody, "titulo">
) => {
  return await apiWrapper(async () => {
    const { data } = await api.patch<MessageType>(
      `/lista/${id}/privacidade`,
      body
    );
    return data;
  }, "change-privacy-by-lista-id");
};

export const changeTituloByListaId = async (
  id: number | string,
  body: Omit<ListaBody, "isPrivate">
) => {
  return await apiWrapper(async () => {
    const { data } = await api.patch<MessageType>(`/lista/${id}/titulo`, body);
    return data;
  }, "change-titulo-by-lista-id");
};

export const toggleLikeByListaId = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/lista/${id}`);
    return data;
  }, "toggle-like-by-lista-id");
};

export const duplicateListaById = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/lista/${id}/duplicate`);
    return data;
  }, "duplicate-lista-by-id");
};
