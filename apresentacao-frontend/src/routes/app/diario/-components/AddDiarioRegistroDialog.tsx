import { useMutation, useQuery } from "@tanstack/react-query";
import { useRouter } from "@tanstack/react-router";
import { useState } from "react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import ComboBox from "@/components/ui/combobox";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";
import { addRegistro } from "@/domain/diario/queries";
import { listJogos } from "@/domain/jogo/queries";
import { Label } from "@/components/ui/label";
import type { JogoId } from "@/domain/jogo/JogoId";

interface AddDiarioRegistroDialogProps {
  jogosRegistrados: JogoId[];
}

const AddDiarioRegistroDialog = ({
  jogosRegistrados,
}: AddDiarioRegistroDialogProps) => {
  [];
  const router = useRouter();

  const [isOpen, setIsOpen] = useState(false);

  const [jogoId, setJogoId] = useState<string | number>("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataTermino, setDataTermino] = useState("");

  const { mutate: addRegistroFn, isPending: isAdding } = useMutation({
    mutationFn: async () => {
      const { error } = await addRegistro({
        jogoId: Number(jogoId),
        dataInicio: dataInicio,
        dataTermino: dataTermino,
      });

      if (error) {
        throw error;
      }
    },
    onSuccess: () => {
      setIsOpen(false);
      router.invalidate();
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const { data: jogos, isPending: jogosIsPending } = useQuery({
    queryKey: ["jogos"],
    queryFn: async () => {
      const { data } = await listJogos();
      return data;
    },
  });

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button>Adicionar Registro</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Adicionar Registro</DialogTitle>
        </DialogHeader>

        <div className="grid grid-cols-2 gap-2 items-center">
          <Label htmlFor="jogoId" className="w-fit">
            Jogo
          </Label>
          {jogosIsPending ? (
            <Skeleton className="w-full h-12" />
          ) : (
            <ComboBox
              disabledItems={
                jogos
                  ?.filter((jogo) =>
                    jogosRegistrados.some((jogoId) => jogoId.id === jogo.id.id)
                  )
                  .map((jogo) => jogo.nome) ?? []
              }
              data={
                jogos?.map((jogo) => ({
                  label: jogo.nome,
                  value: jogo.nome,
                })) ?? []
              }
              onValueChange={(value) =>
                setJogoId(
                  jogos?.find((jogo) => jogo.nome === value)?.id.id ?? ""
                )
              }
              id="jogoId"
              className="w-[200px] ml-auto"
              popoverClassName="w-[200px]"
            />
          )}
        </div>

        <div className="grid grid-cols-2 gap-2 items-center">
          <Label htmlFor="dataInicio" className="w-fit">
            Data Início
          </Label>
          <Input
            id="dataInicio"
            type="date"
            value={dataInicio}
            onChange={(e) => setDataInicio(e.target.value)}
            className="w-[200px] ml-auto"
          />
        </div>

        <div className="grid grid-cols-2 gap-2 items-center">
          <Label htmlFor="dataTermino" className="w-fit">
            Data Término
          </Label>
          <Input
            id="dataTermino"
            type="date"
            value={dataTermino}
            onChange={(e) => setDataTermino(e.target.value)}
            className="w-[200px] ml-auto"
          />
        </div>

        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>

          <Button isLoading={isAdding} onClick={() => addRegistroFn()}>
            Adicionar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default AddDiarioRegistroDialog;
