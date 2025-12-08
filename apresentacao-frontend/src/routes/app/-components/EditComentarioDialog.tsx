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
import { Textarea } from "@/components/ui/textarea";
import type { Comentario } from "@/domain/comentario/Comentario";
import { editComentarioById } from "@/domain/comentario/queries";
import { useQueryClient } from "@tanstack/react-query";
import { Pen } from "lucide-react";
import { useState, useTransition } from "react";
import { toast } from "sonner";

interface EditComentarioDialogProps {
  comentario: Comentario;
}

const EditComentarioDialog = ({ comentario }: EditComentarioDialogProps) => {
  const queryClient = useQueryClient();

  const [text, setText] = useState(comentario.conteudo);
  const [isLoading, startSave] = useTransition();

  function handleSave() {
    startSave(async () => {
      const { data, error } = await editComentarioById(comentario.id.id, {
        text,
      });

      if (data) {
        toast.success(data.message);
        queryClient.invalidateQueries({
          queryKey: ["comentarios"],
        });
        queryClient.invalidateQueries({
          queryKey: ["comentarios-filho"],
        });
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button size="icon" variant="outline">
          <Pen />
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Editar comentário</DialogTitle>
        </DialogHeader>

        <Textarea
          value={text}
          onChange={(e) => setText(e.target.value)}
          rows={6}
          className="resize-none"
        />

        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>
          <Button isLoading={isLoading} onClick={handleSave}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default EditComentarioDialog;
