import { MessageCircle, ThumbsUp } from "lucide-react";

import type { Avaliacao } from "@/domain/jogo/Avaliacao";
import { getUserInfo } from "@/domain/user/queries";
import { useMutation, useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Rating, RatingButton } from "@/components/ui/shadcn-io/rating";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";

import { useUser } from "../../-components/AppContenxt";
import { toggleAvaliacaoLikeById } from "@/domain/jogo/queries";
import { Link, useRouter } from "@tanstack/react-router";
import { toast } from "sonner";
import { cn } from "@/lib/utils";
import CreateComentario from "../../-components/CreateComentario";
import ListComentarios from "../../-components/ListComentarios";

interface AvaliacaoCardProps {
  avaliacao: Avaliacao;
}

const AvaliacaoCard = ({ avaliacao }: AvaliacaoCardProps) => {
  const user = useUser();

  const router = useRouter();

  const { data: autor } = useQuery({
    queryKey: ["autor-avaliacao", avaliacao.id.id],
    queryFn: async () => {
      const { data } = await getUserInfo(avaliacao.autorId.id);

      return data;
    },
  });

  const { mutate: toggleLikeFn, isPending: isLikePending } = useMutation({
    mutationFn: async () => {
      const { data, error } = await toggleAvaliacaoLikeById(
        avaliacao.jogoId.id,
        avaliacao.id.id
      );

      if (error) {
        throw error;
      }

      return data;
    },
    onSuccess: () => {
      router.invalidate();
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  return (
    <div className="rounded-xl border border-zinc-700 bg-zinc-800/50 p-4 flex gap-4">
      <Avatar className="mt-1">
        <AvatarFallback>
          {autor?.nome.substring(0, 1).toUpperCase()}
        </AvatarFallback>
        <AvatarImage src={autor?.avatarUrl} />
      </Avatar>
      <div className="flex-1 flex flex-col gap-4">
        <div className="flex items-center gap-4">
          <div className="flex flex-col gap-0.5">
            <Link
              to={`/app/perfil/$userId`}
              params={{ userId: autor?.userId.id.toString() || "" }}
              target="_blank" 
              className="w-fit"
            >
              <p className="font-semibold capitalize hover:underline w-fit">
                {autor?.nome}
              </p>
            </Link>
            <span className="text-xs text-zinc-500">
              {new Date(avaliacao.data).toLocaleDateString("pt-br")}
            </span>
          </div>

          <div className="ml-auto flex items-center gap-2">
            <Rating value={avaliacao.nota} readOnly>
              {Array.from({ length: 5 }).map((_, index) => (
                <RatingButton className="text-yellow-500" key={index} />
              ))}
            </Rating>

            {
              user.userId.id === avaliacao.autorId.id
              /* <EditComentarioDialog comentario={comentario} /> */
            }
          </div>
        </div>
        <p>{avaliacao.comentario}</p>
        <div className="flex items-start gap-4">
          {/* //TODO: Fazer uma expansao visivel/nao para mostrar os comentarios e criar comentarios */}
          <Accordion
            type="single"
            collapsible
            className="w-full"
            defaultValue="comentarios"
          >
            <AccordionItem value="comentarios">
              <div className="flex items-start gap-4 mb-2">
                <Button
                  variant="ghost"
                  onClick={() => toggleLikeFn()}
                  isLoading={isLikePending}
                >
                  <ThumbsUp
                    className={cn("text-white", {
                      "fill-white text-transparent": avaliacao.curtidas.some(
                        (userId) => userId.id === user.userId.id
                      ),
                    })}
                  />{" "}
                  {avaliacao.curtidas.length}
                </Button>

                <AccordionTrigger className="p-2!">
                  <div className="flex items-center gap-2">
                    <MessageCircle size={15} strokeWidth={2.4} /> Comentários
                  </div>
                </AccordionTrigger>
              </div>
              <AccordionContent className="flex flex-col gap-4 text-balance pt-4 border-t border-zinc-700">
                <CreateComentario createBy="avaliacao" id={avaliacao.id.id} />

                <ListComentarios fetchBy="avaliacao" id={avaliacao.id.id} />
              </AccordionContent>
            </AccordionItem>
          </Accordion>
        </div>
      </div>
    </div>
  );
};

export default AvaliacaoCard;
