export type Conquista = {
  id: ConquistaId;
  nome: string;
  dataDesbloqueada: string | null;
  concluida: boolean;
};

export type ConquistaId = {
  id: number;
};
