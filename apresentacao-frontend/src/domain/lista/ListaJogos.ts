import type { JogoId } from "../jogo/JogoId";
import type { UserId } from "../user/User";

export type ListaId = {
  id: number;
};

export type ListaJogos = {
  id: ListaId;
  donoId: UserId;
  titulo: string;
  isPrivate: boolean;
  jogos: JogoId[];
  curtidas: UserId[];
};
