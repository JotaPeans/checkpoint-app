import type { UserId } from "../user/User";
import type { DiarioId } from "./DiarioId";
import type { RegistroDiario } from "./RegistroDiario";

export type Diario = {
  id: DiarioId;
  donoId: UserId;
  registros: RegistroDiario[];
};
