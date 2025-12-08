import { buttonVariants } from "@/components/ui/button";
import { getListaById } from "@/domain/lista/queries";
import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { ChevronLeft, Gamepad2, MessageCircle } from "lucide-react";
import EditListaName from "./-components/EditListaName";
import EditPrivacy from "./-components/EditPrivacy";
import ModifyJogos from "./-components/ModifyJogos";
import Jogos from "../../jogos/-components/Jogos";
import { useUser } from "../../-components/AppContenxt";
import CreateComentario from "../../-components/CreateComentario";
import ListComentarios from "../../-components/ListComentarios";

export const Route = createFileRoute("/app/listas/$listaId/")({
  component: RouteComponent,
  loader: async ({ params }) => {
    const { data } = await getListaById(params.listaId);

    return data;
  },
});

function RouteComponent() {
  const user = useUser();
  const lista = Route.useLoaderData();
  const navigate = useNavigate();

  if (!lista) {
    navigate({
      to: "/app/listas",
    });
    return;
  }

  return (
    <div className="flex-1 p-4 flex flex-col gap-4">
      <div className="h-20 w-full flex items-center justify-between p-4 px-10 rounded-xl border border-zinc-700">
        <div className="flex items-center gap-4">
          <Link
            to="/app/listas"
            className={buttonVariants({
              variant: "outline",
              size: "icon",
            })}
          >
            <ChevronLeft />
          </Link>
          <div className="flex flex-col">
            <h1 className="text-xl font-semibold">{lista.titulo}</h1>
            <span className="text-xs text-zinc-400">
              {lista.curtidas.length} curtidas
            </span>
          </div>

          {user.userId.id === lista.donoId.id && (
            <EditListaName lista={lista} />
          )}
        </div>

        <div className="flex gap-2 items-center">
          <img src="/logo.png" alt="Checkpoint" width={40} height={93} />
          <h1 className="text-xl font-bold">Checkpoint</h1>
        </div>
      </div>

      <div className="flex-1 flex flex-col gap-4 2xl:px-32">
        {user.userId.id === lista.donoId.id && <EditPrivacy lista={lista} />}

        <div className="pb-4 border-b flex items-center justify-between mt-3">
          <h1 className="font-semibold text-2xl flex items-center gap-2">
            <Gamepad2 /> Jogos
          </h1>
          {user.userId.id === lista.donoId.id && <ModifyJogos lista={lista} />}
        </div>

        <Jogos filterByIds={lista.jogos.map((jogo) => jogo.id)} />

        <div className="pb-4 border-b flex items-center justify-between mt-3">
          <h1 className="font-semibold text-2xl flex items-center gap-2">
            <MessageCircle /> Comentarios
          </h1>
        </div>

        <CreateComentario createBy="lista" id={lista.id.id} />

        <ListComentarios fetchBy="lista" id={lista.id.id} />
      </div>
    </div>
  );
}
