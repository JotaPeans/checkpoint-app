import { api, apiWrapper, type MessageType } from "@/lib/api";
import type { Comentario } from "./Comentario";

export const listComentariosByAvaliacaoId = async (
  avaliacaoId: number | string
) => {
  return await apiWrapper(async () => {
    const { data } = await api.get<Comentario[]>(
      `/comentario/avaliacao/${avaliacaoId}`
    );
    return data;
  }, "list-comentarios-by-avaliacao-id");
};

export const listComentarioByListaId = async (
  listaId: number | string
) => {
  return await apiWrapper(async () => {
    const { data } = await api.get<Comentario[]>(
      `/comentario/lista/${listaId}`
    );
    return data;
  }, "list-comentarios-by-lista-id");
};

export const listComentarioByComentarioPaiId = async (
  comentarioPaiId: number | string
) => {
  return await apiWrapper(async () => {
    const { data } = await api.get<Comentario[]>(
      `/comentario/comentario-pai/${comentarioPaiId}`
    );
    return data;
  }, "list-comentarios-by-comentario-pai-id");
};

interface ComentarioBody {
  text: string;
}

export const createComentarioByAvaliacaoId = async (
  avaliacaoId: number | string,
  body: ComentarioBody
) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(
      `/comentario/avaliacao/${avaliacaoId}`,
      body
    );
    return data;
  }, "create-comentarios-by-avaliacao-id");
};

export const createComentarioByListaId = async (
  listaId: number | string,
  body: ComentarioBody
) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(
      `/comentario/lista/${listaId}`,
      body
    );
    return data;
  }, "create-comentarios-by-lista-id"); 
};

export const createComentarioByComentarioPaiId = async (
  comentarioPaiId: number | string,
  body: ComentarioBody
) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(
      `/comentario/comentario-pai/${comentarioPaiId}`,
      body
    );
    return data;
  }, "create-comentarios-by-comentario-pai-id");
};

export const editComentarioById = async (
  comentarioId: number | string,
  body: ComentarioBody
) => {
  return await apiWrapper(async () => {
    const { data } = await api.put<MessageType>(
      `/comentario/${comentarioId}`,
      body
    );
    return data;
  }, "edit-comentarios-by-id"); 
};

export const toggleLikeComentarioById = async (
  comentarioId: number | string,
) => {
  return await apiWrapper(async () => {
    const { data } = await api.patch<MessageType>(
      `/comentario/${comentarioId}`,
    );
    return data;
  }, "toggle-like-comentario-by-id"); 
};

export const deleteComentarioById = async (
  comentarioId: number | string,
) => {
  return await apiWrapper(async () => {
    const { data } = await api.delete<MessageType>(
      `/comentario/${comentarioId}`,
    );
    return data;
  }, "delete-comentario-by-id"); 
};

