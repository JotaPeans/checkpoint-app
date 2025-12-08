import { api, apiWrapper, type MessageType } from "@/lib/api";

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



