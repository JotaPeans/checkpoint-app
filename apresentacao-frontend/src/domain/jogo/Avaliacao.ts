import type { UserId } from "../user/User";
import type { JogoId } from "./JogoId";

export type AvaliacaoId = {
  id: number;
};

export type Avaliacao = {
  id: AvaliacaoId;
  autorId: UserId;
  jogoId: JogoId;
  nota: number;
  comentario: string;
  data: string;
  curtidas: UserId[];
};
