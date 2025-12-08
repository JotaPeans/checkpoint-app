import type { UserId } from "../user/User";

export type Tag = {
  id: TagId | null;
  nome: string;
  votos: UserId[];
};

export type TagId = {
  id: number;
};
