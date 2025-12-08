import { toast } from "sonner";
import { useRouter } from "@tanstack/react-router";
import { useMutation } from "@tanstack/react-query";
import { Lock, Trash2, Trophy } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import type { Conquista } from "@/domain/diario/Conquista";

import type { RegistroId } from "@/domain/diario/RegistroDiario";
import { cn } from "@/lib/utils";
import { deleteConquistaById } from "@/domain/diario/queries";

import AddConquistaDialog from "./AddConquistaDialog";
import EditConquistaDialog from "./EditConquistaDialog";

interface ConquistasDialogProps {
  conquistas: Conquista[];
  registroId: RegistroId;
}

const ConquistasDialog = ({
  conquistas,
  registroId,
}: ConquistasDialogProps) => {
  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="secondary">
          <Trophy className="h-4 w-4" /> Conquistas
        </Button>
      </DialogTrigger>
      <DialogContent className="max-h-[80%] overflow-y-auto">
        <DialogHeader className="pb-4 border-b">
          <DialogTitle>Conquistas</DialogTitle>
          <AddConquistaDialog registroId={registroId} />
        </DialogHeader>

        <div className="flex flex-col gap-3">
          {conquistas.map((conquista) => (
            <ConquistaCard
              key={conquista.id.id}
              conquista={conquista}
              registroId={registroId}
            />
          ))}
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default ConquistasDialog;

const ConquistaCard = ({
  registroId,
  conquista,
}: {
  registroId: RegistroId;
  conquista: Conquista;
}) => {
  const router = useRouter();

  const { mutate: deleteConquista, isPending } = useMutation({
    mutationFn: async (conquistaId: string | number) => {
      const { data, error } = await deleteConquistaById(
        registroId.id,
        conquistaId
      );

      if (error) throw error;

      return data;
    },
    onSuccess: (data) => {
      router.invalidate();
      toast.success(data?.message);
    },
    onError: (error) => {
      toast.error(error?.message);
    },
  });

  const dataDesbloqueada = conquista.dataDesbloqueada
    ? new Date(conquista.dataDesbloqueada)
    : null;

  if (conquista.concluida && dataDesbloqueada) {
    dataDesbloqueada.setHours(dataDesbloqueada.getHours() + 3);
  }

  return (
    <div
      key={conquista.id.id}
      className={cn(
        "flex items-center gap-3 p-4 rounded-xl border-2 border-zinc-800 text-zinc-600 bg-zinc-900/40",
        {
          "border-2 border-green-800 bg-green-900/15 text-zinc-100":
            conquista.concluida,
        }
      )}
    >
      {conquista.concluida ? (
        <Trophy className="h-4 w-4 text-green-700" />
      ) : (
        <Lock className="h-4 w-4" />
      )}

      <div className="flex flex-col gap-1 font-medium">
        <span>{conquista.nome}</span>
        {conquista.concluida && dataDesbloqueada ? (
          <span className="text-xs text-zinc-500">
            {dataDesbloqueada?.toLocaleDateString("pt-br")} às{" "}
            {dataDesbloqueada?.toLocaleTimeString("pt-br", {
              hour: "2-digit",
              minute: "2-digit",
            })}
          </span>
        ) : (
          <span className="text-xs text-zinc-500">Bloqueada</span>
        )}
      </div>

      <div className="ml-auto">
        <EditConquistaDialog conquista={conquista} />
        <Button
          size="icon"
          variant="ghost"
          className="size-8"
          onClick={() => deleteConquista(conquista.id.id)}
          disabled={isPending}
        >
          <Trash2 className="text-red-700" />
        </Button>
      </div>
    </div>
  );
};
