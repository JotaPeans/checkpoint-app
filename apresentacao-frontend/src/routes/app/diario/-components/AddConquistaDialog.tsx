import { useMutation } from "@tanstack/react-query";
import { useRouter } from "@tanstack/react-router";
import { useState } from "react";
import { toast } from "sonner";

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
import { addConquista } from "@/domain/diario/queries";
import type { RegistroId } from "@/domain/diario/RegistroDiario";

interface AddConquistaDialogProps {
  registroId: RegistroId;
}

const AddConquistaDialog = ({ registroId }: AddConquistaDialogProps) => {
  const router = useRouter();
  const [nomeConquista, setNomeConquista] = useState("");
  const [dataDesbloqueadaConquista, setDataDesbloqueadaConquista] =
    useState("");
  const [conquistaConcluida, setConcluidaConquista] = useState(false);

  const { mutate, isPending } = useMutation({
    mutationFn: async () => {
      const { data, error } = await addConquista({
        registroId: registroId.id,
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
        <Button className="w-fit mt-2">Adicionar Conquista</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogTitle>Adicionar Conquista</DialogTitle>

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
            Adicionar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default AddConquistaDialog;
