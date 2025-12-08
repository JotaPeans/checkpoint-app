import type { AvaliacaoId } from "../jogo/Avaliacao";
import type { ListaId } from "../lista/ListaJogos";
import type { UserId } from "../user/User";

export type Comentario = {
  id: ComentarioId;
  autorId: UserId;
  conteudo: string;
  data: string;
  curtidas: UserId[];

  avaliacaoAlvoId: AvaliacaoId | null;
  listaAlvoId: ListaId | null;
  comentarioPaiId: ComentarioId | null;
};

export type ComentarioId = {
  id: number;
};

