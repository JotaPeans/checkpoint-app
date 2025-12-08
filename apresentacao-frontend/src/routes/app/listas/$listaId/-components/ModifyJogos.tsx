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
import { Skeleton } from "@/components/ui/skeleton";
import { listJogos } from "@/domain/jogo/queries";
import type { ListaJogos } from "@/domain/lista/ListaJogos";
import { updateJogosByListaId } from "@/domain/lista/queries";
import { cn } from "@/lib/utils";
import { useQuery } from "@tanstack/react-query";
import { useRouter } from "@tanstack/react-router";
import { Check, Search } from "lucide-react";
import { useState, useTransition } from "react";
import { toast } from "sonner";

interface ModifyJogosProps {
  lista: ListaJogos;
}

const ModifyJogos = ({ lista }: ModifyJogosProps) => {
  const route = useRouter();
  const [isLoading, startSave] = useTransition();

  const [filter, setFilter] = useState("");

  const [selected, setSelected] = useState(lista.jogos.map((jogo) => jogo.id));

  const { data: jogos, isPending: jogosIsPending } = useQuery({
    queryKey: ["jogos"],
    queryFn: async () => {
      const { data } = await listJogos();
      return data;
    },
  });

  function handleSave() {
    startSave(async () => {
      const { data, error } = await updateJogosByListaId(lista.id.id, {
        jogosIds: selected,
      });

      if (data) {
        toast.success(data.message);
        route.invalidate();
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  const filtered = jogos?.filter((jogo) =>
    jogo.nome.toLowerCase().includes(filter.toLowerCase())
  );

  function toggleSelected(id: number) {
    setSelected((prev) => {
      const newData = [...prev];

      const foundIndex = newData.findIndex((jogo) => jogo === id);

      if (foundIndex >= 0) {
        return newData.filter((_, index) => index !== foundIndex);
      } else {
        return [...newData, id];
      }
    });
  }

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button>
          Modificar Jogos
        </Button>
      </DialogTrigger>
      <DialogContent className="max-h-96 overflow-y-auto">
        <DialogHeader>
          <DialogTitle>Adicionar Jogo a lista</DialogTitle>
        </DialogHeader>

        <div className="relative flex items-center w-full">
          <Search className="absolute left-2 text-zinc-500" size={22} />
          <Input
            className="pl-9 w-full"
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
            placeholder="Pesquisar jogo"
          />
        </div>

        <div className="flex-1 flex flex-col gap-4 max-h-48 overflow-y-auto">
          {jogosIsPending ? (
            <Skeleton className="w-full h-10" />
          ) : (
            filtered?.map((jogo, key) => (
              <div
                key={key}
                className="flex items-center gap-4 cursor-pointer"
                role="button"
                onClick={() => toggleSelected(jogo.id.id)}
              >
                <Check
                  className={cn(
                    selected.includes(jogo.id.id) ? "opacity-100" : "opacity-0"
                  )}
                />
                <span>{jogo.nome}</span>
              </div>
            ))
          )}
        </div>

        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>
          <Button isLoading={isLoading} onClick={handleSave}>
            Adicionar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default ModifyJogos;
