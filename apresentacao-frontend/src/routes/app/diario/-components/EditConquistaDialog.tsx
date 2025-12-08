import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import type { Conquista } from "@/domain/diario/Conquista";
import { Pen } from "lucide-react";
import { useState } from "react";
import { toast } from "sonner";
import { useMutation } from "@tanstack/react-query";
import { useRouter } from "@tanstack/react-router";

import { updateConquista } from "@/domain/diario/queries";

interface EditConquistaDialogProps {
  conquista: Conquista;
}

const EditConquistaDialog = ({
  conquista,
}: EditConquistaDialogProps) => {
  const router = useRouter();

  const currentUnlokedDate = conquista.dataDesbloqueada
    ? new Date(conquista.dataDesbloqueada)
    : null;

  if (currentUnlokedDate) {
    // currentUnlokedDate.setHours(currentUnlokedDate.getHours() - 3);
  }

  const unlockedDateFormatted =
    currentUnlokedDate?.toISOString().split("T")[0] ?? "";

  const [nomeConquista, setNomeConquista] = useState(conquista.nome);
  const [dataDesbloqueadaConquista, setDataDesbloqueadaConquista] = useState(
    unlockedDateFormatted
  );
  const [conquistaConcluida, setConcluidaConquista] = useState(
    conquista.concluida
  );

  const { mutate, isPending } = useMutation({
    mutationFn: async () => {
      const { data, error } = await updateConquista(conquista.id.id, {
        nome: nomeConquista,
        dataDesbloqueada: dataDesbloqueadaConquista,
        concluida: conquistaConcluida,
      });

      if (error) {
        throw error;
      }

      return data;
    },
    onSuccess(data) {
      toast.success(data?.message);
      router.invalidate();
    },
    onError(error) {
      toast.error(error.message);
    },
  });

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button size="icon" className="size-8 text-white" variant="ghost">
          <Pen />
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogTitle>Editar Conquista</DialogTitle>

        <div className="grid grid-cols-2">
          <Label htmlFor="nomeConquista">Nome da conquista</Label>
          <Input
            id="nomeConquista"
            placeholder="nome da conquista"
            value={nomeConquista}
            onChange={(e) => setNomeConquista(e.target.value)}
          />
        </div>
        <div className="grid grid-cols-2">
          <Label htmlFor="dataDesbloqueadaConquista">Data desbloqueada</Label>
          <Input
            id="dataDesbloqueadaConquista"
            placeholder="data desbloqueada"
            type="date"
            value={dataDesbloqueadaConquista}
            onChange={(e) => setDataDesbloqueadaConquista(e.target.value)}
          />
        </div>
        <div className="grid grid-cols-2">
          <Label htmlFor="conquistaConcluida">Concluída</Label>
          <Input
            id="conquistaConcluida"
            type="checkbox"
            className="size-4 ml-auto"
            checked={conquistaConcluida}
            onChange={(e) => setConcluidaConquista(e.target.checked)}
          />
        </div>

        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>
          <Button onClick={() => mutate()} isLoading={isPending}>
            Editar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default EditConquistaDialog;
