import { api, apiWrapper, type MessageType } from "@/lib/api";
import type { Jogo } from "./Jogo";
import type { Tag } from "./Tag";
import type { RequisitosDeSistema } from "./RequisitosDeSistema";

// JOGO
export const getJogoById = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.get<Jogo>(`/jogo/${id}`);
    return data;
  }, "get-jogo-by-id");
};

export const listJogos = async () => {
  return await apiWrapper(async () => {
    const { data } = await api.get<Jogo[]>(`/jogo`);
    return data;
  }, "list-jogos");
};

export const toggleLikeByJogoId = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/jogo/${id}`);
    return data;
  }, "toggle-like-by-jogo-id");
};

// AVALIACAO
export interface AvaliacaoBody {
  nota: number;
  critica: string;
}

export const addAvaliacao = async (
  jogoId: number | string,
  body: AvaliacaoBody
) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(
      `/jogo/${jogoId}/avaliacao`,
      body
    );
    return data;
  }, "add-avaliacao");
};

export const editAvaliacaoById = async (
  jogoId: number | string,
  avaliacaoId: number | string,
  body: AvaliacaoBody
) => {
  return await apiWrapper(async () => {
    const { data } = await api.put<MessageType>(
      `/jogo/${jogoId}/avaliacao/${avaliacaoId}`,
      body
    );
    return data;
  }, "edit-avaliacao-by-id");
};

export const toggleAvaliacaoLikeById = async (
  jogoId: number | string,
  avaliacaoId: number | string
) => {
  return await apiWrapper(async () => {
    const { data } = await api.patch<MessageType>(
      `/jogo/${jogoId}/avaliacao/${avaliacaoId}`
    );

    return data;
  }, "toggle-like-by-avaliacao-id");
};

// TAG

interface AddTagBody {
  tags: string[];
}

interface RemoveTagBody {
  name: string;
}

export const listTopTagsByJogoId = async (jogoId: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.get<Tag[]>(`/jogo/${jogoId}/tag`);
    return data;
  }, "list-top-tags");
};

export const addTag = async (jogoId: number | string, body: AddTagBody) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/jogo/${jogoId}/tag`, body);
    return data;
  }, "add-tag");
};

export const removeTag = async (
  jogoId: number | string,
  body: RemoveTagBody
) => {
  return await apiWrapper(async () => {
    const { data } = await api.delete<MessageType>(`/jogo/${jogoId}/tag`, {
      data: body,
    });
    return data;
  }, "remove-tag");
};

// Requisitos de sistema
export const listRequisitosDeSistemaByJogoId = async (
  jogoId: number | string
) => {
  return await apiWrapper(async () => {
    const { data } = await api.get<RequisitosDeSistema[]>(
      `/jogo/${jogoId}/requisitos`
    );
    return data;
  }, "list-requisitos-de-sistema");
};
