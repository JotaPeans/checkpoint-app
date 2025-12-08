import { useQuery } from "@tanstack/react-query";
import { useRouter } from "@tanstack/react-router";
import { CalendarIcon, CheckIcon, Trash2Icon, Trophy } from "lucide-react";

import ConfirmAction from "@/components/ConfirmAction";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import type { RegistroDiario } from "@/domain/diario/RegistroDiario";
import { deleteRegistroById } from "@/domain/diario/queries";
import { getJogoById } from "@/domain/jogo/queries";
import { formatePercentage } from "@/lib/utils";
import ConquistasDialog from "./ConquistasDialog";

interface RegistroCardProps {
  registro: RegistroDiario;
}

const RegistroCard = ({ registro }: RegistroCardProps) => {
  const router = useRouter();

  const { data: jogo, isLoading: jogoIsLoading } = useQuery({
    queryKey: ["jogo", registro.id],
    queryFn: async () => {
      const { data } = await getJogoById(registro.jogoId.id);
      return data;
    },
  });

  if (!jogo) {
    return null;
  }

  if (jogoIsLoading) {
    return <Skeleton className="h-32 w-full" />;
  }

  return (
    <Card className="w-full">
      <CardHeader className="flex items-center gap-4">
        <img
          src={jogo?.capaUrl}
          alt={jogo?.nome}
          className="h-16 w-16 rounded-xl object-cover"
        />
        <div className="flex flex-col gap-2">
          <CardTitle className="text-xl">{jogo?.nome}</CardTitle>
          <div className="flex items-center gap-4">
            <Badge variant="secondary">
              <CalendarIcon className="h-4 w-4" />
              {new Date(registro.dataInicio).toLocaleDateString("pt-br")}
            </Badge>

            {registro.dataTermino && (
              <Badge variant="secondary">
                <CheckIcon className="h-4 w-4" />
                {new Date(registro.dataTermino).toLocaleDateString("pt-br")}
              </Badge>
            )}

            <Badge variant="secondary">
              <Trophy className="h-4 w-4" />
              {formatePercentage(
                registro.conquistas.length > 0
                  ? registro.conquistas.filter((c) => c.concluida).length /
                      registro.conquistas.length
                  : 0
              )}
            </Badge>
          </div>
        </div>

        <div className="ml-auto flex items-center gap-4">
          <ConquistasDialog
            conquistas={registro.conquistas}
            registroId={registro.id}
          />

          <ConfirmAction
            title="Confirmar exclusão"
            description="Tem certeza que deseja excluir este registro?"
            onConfirm={async () => {
              const { error } = await deleteRegistroById(registro.id.id);

              if (!error) {
                router.invalidate();
              }
            }}
          >
            <Button variant="destructive" size="icon">
              <Trash2Icon className="h-4 w-4" />
            </Button>
          </ConfirmAction>
        </div>
      </CardHeader>
    </Card>
  );
};

export default RegistroCard;
