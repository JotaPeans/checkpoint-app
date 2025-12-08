import {
  createFileRoute,
  Link,
  useNavigate,
  useRouter,
} from "@tanstack/react-router";
import {
  Building2,
  ChevronLeft,
  Cpu,
  Gpu,
  Heart,
  Monitor,
  Ruler,
} from "lucide-react";

import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button, buttonVariants } from "@/components/ui/button";
import { getJogoById, listTopTagsByJogoId } from "@/domain/jogo/queries";
import { useState, useTransition } from "react";
import { useQuery } from "@tanstack/react-query";
import { Skeleton } from "@/components/ui/skeleton";
import SuggestNewTag from "../-components/SuggestNewTag";
import { addJogoFavorito, removeJogoFavorito } from "@/domain/user/queries";
import { useUser } from "../../-components/AppContenxt";
import { toast } from "sonner";
import { cn } from "@/lib/utils";
import ExpandableImage from "@/components/ExpandableImage";
import CreateAvaliacao from "../-components/CreateAvaliacao";
import AvaliacaoCard from "../-components/AvaliacaoCard";

export const Route = createFileRoute("/app/jogos/$jogoId/")({
  component: RouteComponent,
  loader: async ({ params }) => {
    const { data } = await getJogoById(params.jogoId);

    return data;
  },
});

function RouteComponent() {
  const navigate = useNavigate();
  const jogo = Route.useLoaderData();

  if (!jogo) {
    navigate({
      to: "/app/jogos",
    });
    return;
  }

  const user = useUser();
  const [requisitosSelected, setRequisitosSelected] = useState<
    "MINIMO" | "RECOMENDADO"
  >("MINIMO");

  const router = useRouter();

  const {
    isPending: topTagsIsPending,
    data: topTags,
    refetch,
  } = useQuery({
    queryKey: ["jogos-tag", jogo.id.id],
    queryFn: async () => {
      const { data } = await listTopTagsByJogoId(jogo.id.id);

      return data;
    },
  });

  const requisitosMin = jogo.requisitos.find((req) => req.tipo === "MINIMO");
  const requisitosRec = jogo.requisitos.find(
    (req) => req.tipo === "RECOMENDADO"
  );

  const requisitosSistema = {
    MINIMO: {
      sistemaOp: requisitosMin?.sistemaOp,
      processador: requisitosMin?.processador,
      memoria: requisitosMin?.memoria,
      placaVideo: requisitosMin?.placaVideo,
    },
    RECOMENDADO: {
      sistemaOp: requisitosRec?.sistemaOp,
      processador: requisitosRec?.processador,
      memoria: requisitosRec?.memoria,
      placaVideo: requisitosRec?.placaVideo,
    },
  };

  const [isFavLoading, startToggleFavorito] = useTransition();

  const isJogoFavoritoOfCurrentUser = user.jogosFavoritos.some(
    (jogoId) => jogoId.id === jogo.id.id
  );

  function handleToggleJogoFavorito() {
    if (!jogo) return;

    startToggleFavorito(async () => {
      if (isJogoFavoritoOfCurrentUser) {
        const { data, error } = await removeJogoFavorito(jogo.id.id);
        if (data) {
          toast.success(data.message);
          router.invalidate();
        } else if (error) {
          toast.error(error.message);
        }
      } else {
        const { data, error } = await addJogoFavorito(jogo.id.id);
        if (data) {
          toast.success(data.message);
          router.invalidate();
        } else if (error) {
          toast.error(error.message);
        }
      }
    });
  }

  return (
    <div className="flex-1 p-4 flex flex-col gap-4 overflow-hidden">
      <div className="h-20 w-full flex items-center justify-between p-4 px-10 rounded-xl border border-zinc-700">
        <div className="flex items-center gap-4">
          <Link
            to="/app/jogos"
            className={buttonVariants({
              variant: "outline",
              size: "icon",
            })}
          >
            <ChevronLeft />
          </Link>
          <h1 className="text-xl font-semibold">{jogo.nome}</h1>
        </div>

        <div className="flex gap-2 items-center">
          <img src="/logo.png" alt="Checkpoint" width={40} height={93} />
          <h1 className="text-xl font-bold">Checkpoint</h1>
        </div>
      </div>

      <div className="flex-1 flex gap-4 2xl:px-32 overflow-hidden">
        <div className="flex-1 flex flex-col gap-10 overflow-hidden">
          <div className="w-full h-96 rounded-xl relative overflow-hidden bg-zinc-800 flex">
            <img
              src={jogo.capaUrl}
              alt={jogo.nome}
              className="object-cover w-full h-full opacity-60 absolute left-0 right-0"
            />

            <div className="flex-1 p-4 flex flex-col justify-end gap-4 z-10">
              <div className="flex items-center gap-2 overflow-hidden relative w-full">
                {topTagsIsPending ? (
                  <Skeleton className="w-full h-9 bg-zinc-700" />
                ) : (
                  topTags?.map((tag, key) => (
                    <div
                      key={key}
                      className="rounded-full bg-zinc-700 border border-zinc-500 text-zinc-300 px-3 py-0.5 text-sm"
                    >
                      {tag.nome}
                    </div>
                  ))
                )}
                <SuggestNewTag
                  jogo={jogo}
                  refetch={refetch}
                  customText="Sugerir Tag"
                />
              </div>

              <Button
                className="w-fit"
                isLoading={isFavLoading}
                onClick={handleToggleJogoFavorito}
              >
                <Heart
                  className={cn(
                    isJogoFavoritoOfCurrentUser ? "fill-black" : ""
                  )}
                />
                {isJogoFavoritoOfCurrentUser
                  ? "Remover dos favoritos"
                  : "Adicionar aos favoritos"}
              </Button>
            </div>
          </div>

          <div className="flex flex-col gap-4 h-fit overflow-hidden">
            <h3 className="text-xl">Capturas de tela</h3>

            <div className="flex items-center gap-4 overflow-x-auto pb-5">
              {jogo.capturas.map((url, key) => (
                <div className="rounded-lg overflow-hidden min-w-80 h-48 flex items-center justify-center">
                  <ExpandableImage
                    src={url}
                    key={key}
                    alt={key.toString()}
                    className="w-full h-full object-cover"
                  />
                </div>
              ))}
            </div>
          </div>

          <div className="flex flex-col gap-4">
            <div className="flex items-center justify-between">
              <h3 className="text-xl">Avaliações</h3>
              <CreateAvaliacao jogoId={jogo.id.id} />
            </div>
            {jogo.avaliacoes.map((avaliacao, key) => (
              <AvaliacaoCard key={key} avaliacao={avaliacao} />
            ))}
          </div>
        </div>

        <div className="flex-1 max-w-96 flex flex-col gap-4">
          <Card className="w-full max-w-sm">
            <CardHeader>
              <CardTitle className="text-lg">Informações</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex items-center gap-4">
                <Building2 size={20} />
                <div className="flex flex-col gap-1">
                  <span>Desenvolvedor</span>
                  <span className="text-sm text-zinc-500">{jogo.company}</span>
                </div>
              </div>
            </CardContent>
          </Card>

          <Card className="w-full max-w-sm">
            <CardHeader>
              <CardTitle className="text-lg">Requisitos de Sistema</CardTitle>
            </CardHeader>
            <CardContent className="flex flex-col gap-4">
              <Tabs
                defaultValue="MINIMO"
                value={requisitosSelected}
                onValueChange={(v) =>
                  setRequisitosSelected(v as typeof requisitosSelected)
                }
              >
                <TabsList className="w-full">
                  <TabsTrigger value="MINIMO">Mínimos</TabsTrigger>
                  <TabsTrigger value="RECOMENDADO">Recomendados</TabsTrigger>
                </TabsList>
              </Tabs>

              <div className="rounded-lg bg-zinc-800 p-4 flex gap-3">
                <Monitor size={18} className="mt-1" />
                <div className="flex flex-col flex-1">
                  <span>Sistema Operacional</span>
                  <span className="text-sm text-zinc-400">
                    {requisitosSistema[requisitosSelected].sistemaOp}
                  </span>
                </div>
              </div>
              <div className="rounded-lg bg-zinc-800 p-4 flex gap-3">
                <Cpu size={18} className="mt-1" />
                <div className="flex flex-col flex-1">
                  <span>Processador</span>
                  <span className="text-sm text-zinc-400">
                    {requisitosSistema[requisitosSelected].processador}
                  </span>
                </div>
              </div>
              <div className="rounded-lg bg-zinc-800 p-4 flex gap-3">
                <Gpu size={18} className="mt-1" />
                <div className="flex flex-col flex-1">
                  <span>Placa de Vídeo</span>
                  <span className="text-sm text-zinc-400">
                    {requisitosSistema[requisitosSelected].placaVideo}
                  </span>
                </div>
              </div>
              <div className="rounded-lg bg-zinc-800 p-4 flex gap-3">
                <Ruler size={18} className="mt-1" />
                <div className="flex flex-col flex-1">
                  <span>Memória RAM</span>
                  <span className="text-sm text-zinc-400">
                    {requisitosSistema[requisitosSelected].memoria}
                  </span>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
