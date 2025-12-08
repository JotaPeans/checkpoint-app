import { api, apiWrapper, type MessageType } from "@/lib/api";
import type { Diario } from "./Diario";
import type { Conquista } from "./Conquista";

export const getDiario = async () => {
  return await apiWrapper(async () => {
    const { data } = await api.get<Diario>(`/diario`);
    return data;
  }, "get-diario");
};

// Conquista
interface ConquistaBody {
  registroId: number;
  nome: string;
  dataDesbloqueada: string;
  concluida: boolean;
}

export const listConquistas = async () => {
  return await apiWrapper(async () => {
    const { data } = await api.get<Conquista[]>(`/diario/conquista`);
    return data;
  }, "list-conquistas");
};

export const addConquista = async (body: ConquistaBody) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/diario/conquista`, body);
    return data;
  }, "add-conquista");
};

export const updateConquista = async (
  id: string | number,
  body: Omit<ConquistaBody, "registroId">
) => {
  return await apiWrapper(async () => {
    const { data } = await api.put<MessageType>(
      `/diario/conquista/${id}`,
      body
    );
    return data;
  }, "update-conquista");
};

export const deleteConquistaById = async (
  registroId: number | string,
  id: number | string
) => {
  return await apiWrapper(async () => {
    const { data } = await api.delete<MessageType>(
      `/diario/registro/${registroId}/conquista/${id}`
    );
    return data;
  }, "delete-conquista");
};

// Registro
interface RegistroBody {
  jogoId: number;
  dataInicio: string;
  dataTermino: string;
}

export const addRegistro = async (body: RegistroBody) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>(`/diario/registro`, body);
    return data;
  }, "add-registro");
};

export const listRegistros = async () => {
  return await apiWrapper(async () => {
    const { data } = await api.post<RegistroBody[]>(`/diario/registro`);
    return data;
  }, "list-registros");
};

export const updateRegistroById = async (
  id: number | string,
  body: Omit<RegistroBody, "jogoId">
) => {
  return await apiWrapper(async () => {
    const { data } = await api.put<RegistroBody[]>(
      `/diario/registro/${id}`,
      body
    );
    return data;
  }, "update-registro");
};

export const deleteRegistroById = async (id: number | string) => {
  return await apiWrapper(async () => {
    const { data } = await api.delete<MessageType>(`/diario/registro/${id}`);
    return data;
  }, "delete-registro");
};
