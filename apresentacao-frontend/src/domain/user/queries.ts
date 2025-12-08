import { api, apiWrapper, type MessageType } from "@/lib/api";
import type { User } from "./User";

export interface LoginBody {
  email: string;
  senha: string;
}

export interface UpdateAvatarBody {
  avatarUrl: string;
}

export interface TogglePrivacidadeBody {
  isPrivate: boolean;
}

export const getMe = async () => {
  return await apiWrapper(async () => {
    const { data } = await api.get<User>("/user/me");
    return data;
  }, "me");
};

export const getUserInfo = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.get<User>(`/user/info/${id}`);
    return data;
  }, "get-user-info");
};

export const getUsersInfoByIds = async (ids: (number | string)[]) => {
  return await apiWrapper(async () => {
    return await Promise.all(
      ids.map(async (id) => {
        const { data } = await api.get<User>(`/user/info/${id}`);
        return data;
      })
    );
  }, "get-user-info");
};

export const toggleFollow = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/user/toggle-follow/${id}`);
    return data;
  }, "toggle-follow");
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

export const updateAvatar = async (body: UpdateAvatarBody) => {
  return await apiWrapper(async () => {
    const { data } = await api.patch<MessageType>(`/user/avatar`, body);
    return data;
  }, "update-avatar");
};

export const updateProfile = async (body: UpdateAvatarBody) => {
  return await apiWrapper(async () => {
    const { data } = await api.put<MessageType>(`/user/profile`, body);
    return data;
  }, "update-profile");
};

export const togglePrivacidade = async (body: TogglePrivacidadeBody) => {
  return await apiWrapper(async () => {
    const { data } = await api.patch<MessageType>(`/user/privacidade`, body);
    return data;
  }, "toggle-privacidade");
};

export const approveFollow = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/user/approve-follow/${id}`);
    return data;
  }, "approve-follow");
};

export const rejectFollow = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/user/reject-follow/${id}`);
    return data;
  }, "reject-follow");
};
