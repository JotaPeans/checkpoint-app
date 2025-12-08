import type { JogoId } from "../jogo/JogoId";
import type { Conquista } from "./Conquista";
import type { DiarioId } from "./DiarioId";

export type RegistroDiario = {
  id: RegistroId;
  jogoId: JogoId;
  diarioId: DiarioId;
  dataInicio: string;
  dataTermino: string | null;
  conquistas: Conquista[];
};

export type RegistroId = {
  id: number;
};
