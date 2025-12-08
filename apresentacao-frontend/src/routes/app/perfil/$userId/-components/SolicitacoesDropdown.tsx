import { Check, Users, X } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useUser } from "@/routes/app/-components/AppContenxt";
import { useMutation, useQuery } from "@tanstack/react-query";
import {
  approveFollow,
  getUsersInfoByIds,
  rejectFollow,
} from "@/domain/user/queries";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import type { UserId } from "@/domain/user/User";
import { useRouter } from "@tanstack/react-router";
import { toast } from "sonner";
import { Badge } from "@/components/ui/badge";

const SolicitacoesDropdown = () => {
  const router = useRouter();
  const authenticatedUser = useUser();

  const { data: solicitacoesPendentes, isPending: solicitacoesIsPending } =
    useQuery({
      queryKey: [
        "users",
        "solicitacoes",
        authenticatedUser.solicitacoesPendentes.length,
      ],
      queryFn: async () => {
        const { data } = await getUsersInfoByIds(
          authenticatedUser?.solicitacoesPendentes.map(
            (solicitacao) => solicitacao.id
          ) || []
        );
        return data;
      },
    });

  const { mutate: approveFollowMutate, isPending: approveFollowIsPending } =
    useMutation({
      mutationFn: async (userId: UserId) => {
        const { data, error } = await approveFollow(userId.id);

        if (error) {
          throw error;
        }

        return data;
      },
      onSuccess() {
        router.invalidate();
      },
      onError: (error) => {
        toast.error(error.message);
      },
    });

  const { mutate: rejectFollowMutate, isPending: rejectFollowIsPending } =
    useMutation({
      mutationFn: async (userId: UserId) => {
        const { data, error } = await rejectFollow(userId.id);

        if (error) {
          throw error;
        }

        return data;
      },
      onSuccess() {
        router.invalidate();
      },
      onError: (error) => {
        toast.error(error.message);
      },
    });

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="outline" size="icon" className="relative">
          <Users />

          {authenticatedUser?.solicitacoesPendentes.length > 0 && (
            <Badge className="absolute -top-2.5 -right-3 py-0 px-1.5 bg-red-500 text-white text-xs font-bold">
              {authenticatedUser?.solicitacoesPendentes.length}
            </Badge>
          )}
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="w-80" align="start">
        <DropdownMenuLabel>Solicitações pendentes</DropdownMenuLabel>
        <DropdownMenuSeparator />
        <DropdownMenuGroup>
          {solicitacoesIsPending ? (
            <span className="text-zinc-400 p-1 text-sm">Carregando...</span>
          ) : (
            solicitacoesPendentes?.map((user) => (
              <div key={user.userId.id} className="p-2 flex items-center gap-4">
                <Avatar>
                  <AvatarImage
                    src={user.avatarUrl}
                    alt={user.nome}
                    width={3000}
                    height={3000}
                    className="object-cover"
                  />
                  <AvatarFallback>{user.nome.substring(0, 2)}</AvatarFallback>
                </Avatar>
                <div className="flex flex-col overflow-hidden">
                  <p className="truncate">{user.nome}</p>
                  <p className="text-xs text-zinc-400 truncate">{user.email}</p>
                </div>

                <div className="ml-auto flex items-center gap-2">
                  <Button
                    variant="ghost"
                    size="icon"
                    className="size-8 bg-linear-to-r from-blue-600 to-blue-900 text-white hover:scale-105"
                    onClick={() => approveFollowMutate(user.userId)}
                    isLoading={approveFollowIsPending}
                  >
                    <Check />
                  </Button>
                  <Button
                    variant="destructive"
                    size="icon"
                    className="size-8 hover:scale-105"
                    onClick={() => rejectFollowMutate(user.userId)}
                    isLoading={rejectFollowIsPending}
                  >
                    <X />
                  </Button>
                </div>
              </div>
            ))
          )}

          {solicitacoesPendentes?.length === 0 && (
            <span className="text-zinc-400 p-1 text-sm">
              Nenhuma solicitação pendente
            </span>
          )}
        </DropdownMenuGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default SolicitacoesDropdown;
