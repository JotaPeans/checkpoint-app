import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button, buttonVariants } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getUserInfo, toggleFollow } from "@/domain/user/queries";
import type { User } from "@/domain/user/User";
import { useMutation, useQuery } from "@tanstack/react-query";
import { createFileRoute, Link, redirect } from "@tanstack/react-router";
import { ChevronLeft, Lock, UserPlus } from "lucide-react";
import { toast } from "sonner";
import Jogos from "../../jogos/-components/Jogos";
import { useUser } from "../../-components/AppContenxt";
import { listListasByIds } from "@/domain/lista/queries";
import ListaCard from "../../listas/-components/ListaCard";
import SolicitacoesDropdown from "./-components/SolicitacoesDropdown";
import EditProfile from "./-components/EditProfile";
import EditAvatar from "./-components/EditAvatar";

export const Route = createFileRoute("/app/perfil/$userId/")({
  component: RouteComponent,
  loader: async ({ params }) => {
    const { data, error } = await getUserInfo(params.userId);

    if (!data || error) {
      throw redirect({ to: "/app/jogos" });
    }

    return data;
  },
});

function RouteComponent() {
  const user = Route.useLoaderData();

  const authenticatedUser = useUser();

  return (
    <div className="flex-1 p-4 flex flex-col gap-4">
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
          <h1 className="text-xl font-semibold">perfil de Usuário</h1>

          {user.userId.id === authenticatedUser.userId.id && (
            <SolicitacoesDropdown />
          )}
        </div>

        <div className="flex gap-2 items-center">
          <img src="/logo.png" alt="Checkpoint" width={40} height={93} />
          <h1 className="text-xl font-bold">Checkpoint</h1>
        </div>
      </div>

      <div className="flex-1 flex flex-col gap-5 2xl:px-32 mt-5">
        {user.email === null ? (
          <PrivateProfile user={user} />
        ) : (
          <PublicProfile user={user} />
        )}
      </div>
    </div>
  );
}

const PublicProfile = ({ user }: { user: User }) => {
  const authenticatedUser = useUser();

  const { data: listas, refetch: refetchListas } = useQuery({
    queryKey: ["listas-user", user.userId.id],
    queryFn: async () => {
      const { data } = await listListasByIds(
        user.listas.map((lista) => lista.id)
      );
      return data;
    },
  });

  return (
    <div className="flex-1 flex flex-col gap-4">
      <Card className="px-6">
        <div className="w-full flex items-center flex-col lg:flex-row gap-4">
          <div className="flex items-center gap-4">
            <Avatar className="size-24">
              {authenticatedUser.userId.id === user.userId.id ? (
                <EditAvatar />
              ) : (
                <>
                  <AvatarImage
                    src={user.avatarUrl}
                    alt={user.nome}
                    width={3000}
                    height={3000}
                    className="object-cover"
                  />
                  <AvatarFallback>{user.nome.substring(0, 2)}</AvatarFallback>
                </>
              )}
            </Avatar>
            <div className="flex flex-col gap-4">
              <div>
                <h2 className="text-3xl font-bold text-zinc-50">{user.nome}</h2>
                <p className="text-sm text-zinc-400">{user.email}</p>
              </div>

              <div className="flex items-center gap-4">
                <div className="flex flex-col items-center">
                  <p className="text-2xl font-bold">{user.seguidores.length}</p>
                  <p className="text-sm text-zinc-400">Seguidores</p>
                </div>
                <div className="flex flex-col items-center">
                  <p className="text-2xl font-bold">{user.seguindo.length}</p>
                  <p className="text-sm text-zinc-400">Seguindo</p>
                </div>
              </div>
            </div>
          </div>
          <p className="text-justify">{user.bio}</p>

          {authenticatedUser.userId.id === user.userId.id && <EditProfile />}
        </div>
      </Card>

      <Card>
        <CardHeader className="flex items-center justify-between">
          <CardTitle className="text-2xl font-bold">Jogos Favoritos</CardTitle>
        </CardHeader>
        <CardContent>
          <Jogos filterByIds={user.jogosFavoritos.map((jogoId) => jogoId.id)} />
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-2xl font-bold">Listas</CardTitle>
        </CardHeader>
        <CardContent className="grid grid-cols-3">
          {listas?.map((lista, key) => (
            <ListaCard
              key={key}
              lista={lista}
              refetchListas={refetchListas}
              linkDisabled
            />
          ))}
        </CardContent>
      </Card>
    </div>
  );
};

const PrivateProfile = ({ user }: { user: User }) => {
  const { mutate, isPending } = useMutation({
    mutationFn: async () => {
      const { data, error } = await toggleFollow(user.userId.id);

      if (error) throw error;

      return data;
    },
    onSuccess: (data) => {
      toast.success(data?.message);
    },
    onError: (error) => {
      toast.error(error?.message);
    },
  });

  return (
    <div className="rounded-xl border border-zinc-700 p-6 w-full md:w-[500px] mx-auto flex flex-col items-center gap-4">
      <div className="rounded-full p-4 bg-zinc-800 w-fit">
        <Lock size={48} className="text-zinc-500" />
      </div>

      <h2 className="text-2xl font-bold text-zinc-50">{user.nome}</h2>

      <p className="text-sm text-zinc-400">Esse perfil é privado</p>
      <p className="text-sm text-zinc-400">
        Siga este usuário para ver suas listas e informações
      </p>

      <Button
        variant="secondary"
        onClick={() => mutate()}
        isLoading={isPending}
      >
        <UserPlus />
        Solicitar para Seguir
      </Button>
    </div>
  );
};
