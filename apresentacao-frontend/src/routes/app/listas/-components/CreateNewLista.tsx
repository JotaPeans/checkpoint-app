import { useQueryClient } from "@tanstack/react-query";
import { useState, useTransition } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { createLista } from "@/domain/lista/queries";

const CreateNewLista = () => {
  const queryClient = useQueryClient();

  const [listaNome, setListaNome] = useState("");
  const [isListaPrivate, setIsListaPrivate] = useState(false);

  const [isLoading, startSave] = useTransition();

  function handleSave() {
    startSave(async () => {
      const { data, error } = await createLista({
        titulo: listaNome,
        isPrivate: isListaPrivate,
      });

      if (data) {
        toast.success(data.message);
        queryClient.invalidateQueries({
          queryKey: ["listas"]
        })
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button className="px-10">Criar lista</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Criar nova lista</DialogTitle>
          <DialogDescription>
            Insira as informações da nova lista
          </DialogDescription>
        </DialogHeader>

        <div className="flex flex-col gap-2">
          <Label>Nome da lista</Label>
          <Input
            placeholder="insira o nome da lista"
            value={listaNome}
            onChange={(e) => setListaNome(e.target.value)}
          />
        </div>

        <div className="flex flex-col gap-2">
          <Label>Lista Privada?</Label>
          <Switch
            className="h-6 w-10"
            thumbClassName="size-5"
            checked={isListaPrivate}
            onCheckedChange={(v) => setIsListaPrivate(v)}
          />
        </div>

        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>
          <Button onClick={handleSave} isLoading={isLoading}>
            Criar lista
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default CreateNewLista;
