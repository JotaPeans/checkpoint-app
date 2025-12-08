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
import type { Jogo } from "@/domain/jogo/Jogo";
import { addTag } from "@/domain/jogo/queries";
import { Plus } from "lucide-react";
import { useState, useTransition } from "react";
import { toast } from "sonner";

interface SuggestNewTagProps {
  jogo: Jogo;
  customText?: string;
  refetch: () => Promise<any>;
}

const SuggestNewTag = ({ jogo, refetch, customText }: SuggestNewTagProps) => {
  const [tag, setTag] = useState("");
  const [isloading, startSave] = useTransition();

  function handleSave() {
    startSave(async () => {
      const { data, error } = await addTag(jogo.id.id, {
        tags: [tag],
      });

      if (data) {
        toast.success(data.message);
        await refetch();
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  return (
    <Dialog>
      {customText ? (
        <DialogTrigger asChild>
          <Button variant="secondary">
            <Plus /> {customText}
          </Button>
        </DialogTrigger>
      ) : (
        <DialogTrigger asChild>
          <Button size="icon" variant="secondary">
            <Plus />
          </Button>
        </DialogTrigger>
      )}
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Sugerir Nova Tag</DialogTitle>
          <DialogDescription>
            Sugira uma tag relevante para {jogo.nome}
          </DialogDescription>
        </DialogHeader>

        <Label>Nome da Tag</Label>
        <Input
          placeholder="insira o nome da tag"
          value={tag}
          onChange={(e) => setTag(e.target.value)}
        />

        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>
          <Button isLoading={isloading} onClick={handleSave}>
            Enviar sugestão
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default SuggestNewTag;
