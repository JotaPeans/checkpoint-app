import { apiWrapper, api, type MessageType } from "@/lib/api.ts";

interface DadosCadastro {
  nome: string;
  email: string;
  senha: string;
}

export const cadastrar = async (dados: DadosCadastro) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<MessageType>("/user/cadastro", dados);
    return data;
  }, "cadastrar");
};

interface DadosLogin {
  email: string;
  senha: string;
}

export const login = async (dados: DadosLogin) => {
  return await apiWrapper(async () => {
    const { data } = await api.post<{ token: string }>("/user/login", dados);
    return data;
  }, "login");
};
