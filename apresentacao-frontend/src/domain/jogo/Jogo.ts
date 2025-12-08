import type { UserId } from "../user/User";
import type { Avaliacao } from "./Avaliacao";
import type { JogoId } from "./JogoId";
import type { RequisitosDeSistema } from "./RequisitosDeSistema";
import type { Tag } from "./Tag";

export type Jogo = {
  id: JogoId;
  nome: string;
  company: string;
  capaUrl: string;
  informacaoTitulo: string;
  informacaoDescricao: string;
  nota: number;
  capturas: string[];
  curtidas: UserId[];
  tags: Tag[];
  requisitos: RequisitosDeSistema[];
  avaliacoes: Avaliacao[];
};
