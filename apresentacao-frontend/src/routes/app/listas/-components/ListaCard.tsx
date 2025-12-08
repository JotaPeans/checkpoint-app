import type { ListaJogos } from "@/domain/lista/ListaJogos";
import { Dot, Globe, Heart, Lock, Trash2, User } from "lucide-react";
import { useUser } from "../../-components/AppContenxt";
import { Button, buttonVariants } from "@/components/ui/button";
import { useTransition } from "react";
import { deleteListaById, toggleLikeByListaId } from "@/domain/lista/queries";
import { toast } from "sonner";
import { cn } from "@/lib/utils";
import { Link } from "@tanstack/react-router";
import ConfirmAction from "@/components/ConfirmAction";
import { useQuery } from "@tanstack/react-query";
import { getUserInfo } from "@/domain/user/queries";
import { Skeleton } from "@/components/ui/skeleton";

interface ListaCardProps {
  lista: ListaJogos;
  refetchListas: () => Promise<any>;
  linkDisabled?: boolean;
}

const privacyMap = {
  true: (
    <span className="flex items-center gap-2 text-sm rounded-full bg-zinc-700 px-3 py-1">
      <Lock size={18} />
      Privada
    </span>
  ),
  false: (
    <span className="flex items-center gap-2 text-sm rounded-full bg-green-900 px-3 py-1">
      <Globe size={18} />
      Pública
    </span>
  ),
};

const ListaCard = ({
  lista,
  refetchListas,
  linkDisabled = false,
}: ListaCardProps) => {
  const user = useUser();

  const [isLikePending, startLike] = useTransition();

  const { data: dono, isPending: donoIsPending } = useQuery({
    queryKey: ["dono-lista", lista.id.id],
    queryFn: async () => {
      const { data } = await getUserInfo(lista.donoId.id);

      return data;
    },
  });

  function handleLike() {
    startLike(async () => {
      const { data, error } = await toggleLikeByListaId(lista.id.id);

      if (data) {
        refetchListas();
      }
      if (error) {
        toast.error(error.message);
      }
    });
  }

  async function handleDelete() {
    const { data, error } = await deleteListaById(lista.id.id);

    if (data) {
      refetchListas();
    }
    if (error) {
      toast.error(error.message);
    }
  }

  return (
    <div className="rounded-xl border border-zinc-700 bg-zinc-800 p-5 flex flex-col gap-4">
      <div className="flex items-center justify-between">
        <span className="capitalize text-xl">{lista.titulo}</span>
        {privacyMap[`${lista.isPrivate}`]}
      </div>

      <div className="flex items-center justify-between">
        <div className="flex items-center gap-1">
          <div className="flex items-center gap-2">
            <User size={18} />
            {donoIsPending ? (
              <Skeleton />
            ) : dono?.userId.id == user.userId.id ? (
              <span className="capitalize">Você</span>
            ) : (
              <Link
                to={`/app/perfil/$userId`}
                params={{ userId: dono?.userId.id.toString() || "" }}
                target="_blank"
                className="w-fit"
                disabled={linkDisabled}
              >
                <p
                  className={cn("font-semibold capitalize w-fit", {
                    "hover:underline": !linkDisabled,
                  })}
                >
                  {dono?.nome}
                </p>
              </Link>
            )}
          </div>
          <Dot />
          <div className="flex items-center gap-2">
            <span>{lista.jogos.length} jogos</span>
          </div>
        </div>

        <Button variant="ghost" onClick={handleLike} isLoading={isLikePending}>
          <Heart
            className={cn(
              lista.curtidas.some((userId) => userId.id === user.userId.id)
                ? "fill-white"
                : ""
            )}
          />{" "}
          <span>{lista.curtidas.length}</span>
        </Button>
      </div>

      <div className="flex items-center gap-4">
        <Link
          to="/app/listas/$listaId"
          params={{
            listaId: lista.id.id.toString(),
          }}
          className={buttonVariants({
            className: "flex-1",
          })}
        >
          Abrir lista
        </Link>

        {user?.userId.id == dono?.userId.id && (
          <ConfirmAction
            title={lista.titulo}
            description={`Tem certeza de que quer excluir a lista ${lista.titulo}`}
            onConfirm={handleDelete}
          >
            <Button variant="destructive" size="icon">
              <Trash2 />
            </Button>
          </ConfirmAction>
        )}
      </div>
    </div>
  );
};

export default ListaCard;
