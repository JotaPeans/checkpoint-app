import type { DiarioId } from "../diario/DiarioId";
import type { JogoId } from "../jogo/JogoId";
import type { ListaId } from "../lista/ListaJogos";

export type UserId = {
  id: number;
};

export type RedeSocial = {
  username: string;
  plataforma: string;
};

export type User = {
  userId: UserId;
  email: string;
  nome: string;
  senha: string;
  avatarUrl: string;
  bio: string;
  isPrivate: boolean;
  emailVerificado: boolean;
  diarioId: DiarioId;

  solicitacoesPendentes: UserId[];
  redesSociais: RedeSocial[];
  seguindo: UserId[];
  seguidores: UserId[];
  listas: ListaId[];
  jogosFavoritos: JogoId[];
};
