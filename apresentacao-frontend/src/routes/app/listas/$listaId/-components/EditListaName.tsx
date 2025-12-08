import { Button } from "@/components/ui/button";
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
import { Label } from "@/components/ui/label";
import type { ListaJogos } from "@/domain/lista/ListaJogos";
import { changeTituloByListaId } from "@/domain/lista/queries";
import { useRouter } from "@tanstack/react-router";
import { Pen } from "lucide-react";
import { useState, useTransition } from "react";
import { toast } from "sonner";

interface EditListaNameProps {
  lista: ListaJogos;
}

const EditListaName = ({ lista }: EditListaNameProps) => {
  const router = useRouter();
  const [titulo, setTitulo] = useState(lista.titulo);
  const [isLoading, startEdit] = useTransition();

  function handleSave() {
    startEdit(async () => {
      const { data, error } = await changeTituloByListaId(lista.id.id, {
        titulo,
      });

      if (data) {
        toast.success(data.message);
        router.invalidate();
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button size="icon" variant="ghost">
          <Pen />
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Editar nome da lista</DialogTitle>
        </DialogHeader>

        <Label>Nome da lista</Label>
        <Input
          placeholder="insira o nome da lista"
          value={titulo}
          onChange={(e) => setTitulo(e.target.value)}
        />

        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>
          <Button isLoading={isLoading} onClick={handleSave}>
            Editar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default EditListaName;
