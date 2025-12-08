import { api, apiWrapper, type MessageType } from "@/lib/api";



export const toggleLikeByJogoId = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/jogo/${id}`);
    return data;
  }, "toggle-like-by-jogo-id");
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
