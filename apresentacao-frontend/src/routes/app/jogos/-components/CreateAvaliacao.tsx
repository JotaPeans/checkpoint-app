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
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { addAvaliacao } from "@/domain/jogo/queries";
import { Plus } from "lucide-react";
import { useState, useTransition } from "react";
import { toast } from "sonner";
import { Rating, RatingButton } from "@/components/ui/shadcn-io/rating";
import { useRouter } from "@tanstack/react-router";

interface CreateAvaliacaoProps {
  jogoId: number | string;
}

const CreateAvaliacao = ({ jogoId }: CreateAvaliacaoProps) => {
  const router = useRouter();
  const [critica, setCritica] = useState("");
  const [nota, setNota] = useState(0);
  const [isLoading, startPublish] = useTransition();

  function handlePublish() {
    startPublish(async () => {
      const { data, error } = await addAvaliacao(jogoId, {
        critica,
        nota,
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
        <Button>
          <Plus /> Adicionar Avaliação
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Avaliação</DialogTitle>
        </DialogHeader>

        <div className="flex flex-col gap-2">
          <Label htmlFor="critica">Crítica</Label>
          <Textarea
            id="critica"
            className="resize-none"
            rows={6}
            placeholder="Insira sua crítica"
            value={critica}
            onChange={(e) => setCritica(e.target.value)}
          />
        </div>

        <div className="flex flex-col gap-2">
          <Label htmlFor="critica">Nota</Label>
          <Rating defaultValue={nota} value={nota} onValueChange={setNota}>
            {Array.from({ length: 5 }).map((_, index) => (
              <RatingButton className="text-yellow-500" key={index} />
            ))}
          </Rating>
        </div>

        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>
          <Button isLoading={isLoading} onClick={handlePublish}>
            Publicar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default CreateAvaliacao;
