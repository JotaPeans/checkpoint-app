import { api, apiWrapper, type MessageType } from "@/lib/api";
import type { User } from "./User";

export const getMe = async () => {
  return await apiWrapper(async () => {
    const { data } = await api.get<User>("/user/me");
    return data;
  }, "me");
};

export const addJogoFavorito = async (jogoId: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(
      `/user/jogo-favorito/${jogoId}`
    );
    return data;
  }, "add-jogo-favorito");
};

export const removeJogoFavorito = async (jogoId: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.delete<MessageType>(
      `/user/jogo-favorito/${jogoId}`
    );
    return data;
  }, "remove-jogo-favorito");
};
