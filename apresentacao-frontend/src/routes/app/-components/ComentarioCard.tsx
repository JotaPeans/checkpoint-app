import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { ThumbsUp, Trash2 } from "lucide-react";
import { toast } from "sonner";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import type { Comentario } from "@/domain/comentario/Comentario";
import {
  deleteComentarioById,
  listComentarioByComentarioPaiId,
  toggleLikeComentarioById,
} from "@/domain/comentario/queries";
import ConfirmAction from "@/components/ConfirmAction";
import { cn } from "@/lib/utils";
import { getUserInfo } from "@/domain/user/queries";

import ReplyComentarioDialog from "./ReplyComentarioDialog";
import { useUser } from "./AppContenxt";
import EditComentarioDialog from "./EditComentarioDialog";
import { Link } from "@tanstack/react-router";

interface ComentarioCardProps {
  comentario: Comentario;
}

const ComentarioCard = ({ comentario }: ComentarioCardProps) => {
  const user = useUser();
  const queryClient = useQueryClient();

  const { data: autor } = useQuery({
    queryKey: ["autor-comentario", comentario.id.id],
    queryFn: async () => {
      const { data } = await getUserInfo(comentario.autorId.id);

      return data;
    },
  });

  const { data: comentarios } = useQuery({
    queryKey: ["comentarios-filho", comentario.id.id],
    queryFn: async () => {
      const { data } = await listComentarioByComentarioPaiId(comentario.id.id);

      return data;
    },
  });

  async function handleDelete() {
    const { data, error } = await deleteComentarioById(comentario.id.id);

    if (data) {
      toast.success(data.message);
      queryClient.invalidateQueries({
        queryKey: ["comentarios"],
      });
    } else if (error) {
      toast.error(error.message);
    }
  }

  const { mutate: toggleLike, isPending: isTogglingLike } = useMutation({
    mutationFn: async () => {
      const { data, error } = await toggleLikeComentarioById(comentario.id.id);

      if (error) {
        throw error;
      }

      return data;
    },
    onSuccess(data) {
      toast.success(data?.message);
      queryClient.invalidateQueries({
        queryKey: ["comentarios"],
      });
    },
    onError(error) {
      toast.error(error.message);
    },
  });

  return (
    <>
      <div className="rounded-xl border border-zinc-700 bg-zinc-800/50 p-4 flex gap-4">
        <Avatar>
          <AvatarFallback>
            {autor?.nome.substring(0, 1).toUpperCase()}
          </AvatarFallback>
          <AvatarImage src={autor?.avatarUrl} />
        </Avatar>
        <div className="flex-1 flex flex-col gap-4">
          <div className="flex items-center gap-4">
            <Link
              to={`/app/perfil/$userId`}
              params={{ userId: autor?.userId.id.toString() || "" }}
              target="_blank"
            >
              <p className="font-semibold capitalize hover:underline">{autor?.nome}</p>
            </Link>
            <span className="text-sm text-zinc-500">
              {new Date(comentario.data).toLocaleDateString("pt-br")}
            </span>

            {user.userId.id === comentario.autorId.id && (
              <div className="ml-auto flex items-center gap-2">
                <EditComentarioDialog comentario={comentario} />
                <ConfirmAction
                  title="Excluir comentario"
                  description="Essa ação não poderá ser desfeita"
                  onConfirm={handleDelete}
                >
                  <Button size="icon" variant="destructive">
                    <Trash2 />
                  </Button>
                </ConfirmAction>
              </div>
            )}
          </div>
          <p>{comentario.conteudo}</p>
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              onClick={() => toggleLike()}
              isLoading={isTogglingLike}
            >
              <ThumbsUp
                className={cn("text-white", {
                  "fill-white text-transparent": comentario.curtidas.some(
                    (userId) => userId.id === user.userId.id
                  ),
                })}
              />{" "}
              {comentario.curtidas.length}
            </Button>

            <ReplyComentarioDialog comentarioPaiId={comentario.id.id} />
          </div>
        </div>
      </div>

      {comentarios && comentarios.length > 0 ? (
        <div className="pl-16 flex flex-col gap-4">
          {comentarios?.map((subComentario, key) => (
            <ComentarioCard comentario={subComentario} key={key} />
          ))}
        </div>
      ) : null}
    </>
  );
};

export default ComentarioCard;
