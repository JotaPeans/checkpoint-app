import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import {
  createComentarioByAvaliacaoId,
  createComentarioByComentarioPaiId,
  createComentarioByListaId,
} from "@/domain/comentario/queries";
import { useQueryClient } from "@tanstack/react-query";
import { useState, useTransition } from "react";
import { toast } from "sonner";

interface CreateComentarioProps {
  createBy: "avaliacao" | "lista" | "pai";
  id: number | string;
}

const createComentarioMapper = {
  avaliacao: createComentarioByAvaliacaoId,
  lista: createComentarioByListaId,
  pai: createComentarioByComentarioPaiId,
};

const CreateComentario = ({ createBy, id }: CreateComentarioProps) => {
  const queryClient = useQueryClient();
  const [comentario, setComentario] = useState("");
  const [isLoading, startPublish] = useTransition();

  function handlePublish() {
    const createComentario = createComentarioMapper[createBy];
    startPublish(async () => {
      const { data, error } = await createComentario(id, {
        text: comentario,
      });

      if (data) {
        toast.success(data.message);
        queryClient.invalidateQueries({
          queryKey: ["comentarios"]
        })
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  return (
    <div className="rounded-xl border border-zinc-700 bg-zinc-800/50 p-4 flex flex-col gap-4 ">
      <Textarea
        className="resize-none"
        rows={6}
        placeholder="Insira seu comentário"
        value={comentario}
        onChange={(e) => setComentario(e.target.value)}
      />
      <Button
        className="w-fit ml-auto"
        isLoading={isLoading}
        onClick={handlePublish}
      >
        Publicar
      </Button>
    </div>
  );
};

export default CreateComentario;
