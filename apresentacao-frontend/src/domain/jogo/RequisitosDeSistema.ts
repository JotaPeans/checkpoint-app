import type { JogoId } from "./JogoId";

export type RequisitosDeSistema = {
  id: RequisitosDeSistemaId;
  jogoId: JogoId;
  sistemaOp: string;
  processador: string;
  memoria: string;
  placaVideo: string;
  tipo: string;
};

export type RequisitosDeSistemaId = {
  id: number;
};
