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
import { createComentarioByComentarioPaiId } from "@/domain/comentario/queries";
import { useQueryClient } from "@tanstack/react-query";
import { MessageCircle } from "lucide-react";
import { useState, useTransition } from "react";
import { toast } from "sonner";

interface ReplyComentarioDialogProps {
  comentarioPaiId: string | number;
}

const ReplyComentarioDialog = ({
  comentarioPaiId,
}: ReplyComentarioDialogProps) => {
  const queryClient = useQueryClient();
  const [text, setText] = useState("");

  const [isloading, startReply] = useTransition();

  function handleReply() {
    startReply(async () => {
      const { data, error } = await createComentarioByComentarioPaiId(
        comentarioPaiId,
        {
          text,
        }
      );

      if (data) {
        toast.success(data.message);
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
        <Button variant="ghost">
          <MessageCircle /> Responder
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Responder o comentario</DialogTitle>
        </DialogHeader>

        <Textarea
          rows={5}
          className="resize-none"
          value={text}
          onChange={(e) => setText(e.target.value)}
        />

        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>
          <Button isLoading={isloading} onClick={handleReply}>
            Enviar resposta
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default ReplyComentarioDialog;
