import { createFileRoute, Link } from "@tanstack/react-router";
import { Book, List, Search, User } from "lucide-react";
import { useState } from "react";

import { buttonVariants } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

import Jogos from "./-components/Jogos";
import { useUser } from "../-components/AppContenxt";

export const Route = createFileRoute("/app/jogos/")({
  component: RouteComponent,
});

function RouteComponent() {
  const authenticatedUser = useUser();
  const [jogoFiltro, setJogoFiltro] = useState("");

  return (
    <div className="flex-1 p-4 flex flex-col gap-4">
      <div className="h-20 w-full flex items-center justify-between p-4 px-10 rounded-xl border border-zinc-700">
        <div className="flex gap-2 items-center">
          <img src="/logo.png" alt="Checkpoint" width={40} height={93} />
          <h1 className="text-xl font-bold">Checkpoint</h1>
        </div>

        <div className="relative flex items-center">
          <Search className="absolute left-2 text-zinc-500" size={22} />
          <Input
            className="max-w-96 pl-9"
            value={jogoFiltro}
            onChange={(e) => setJogoFiltro(e.target.value)}
            placeholder="Pesquisar jogo"
          />
        </div>

        <div className="flex items-center gap-4">
          <Link
            to="/app/listas"
            className={buttonVariants({
              variant: "outline",
            })}
          >
            <List /> Listas
          </Link>
          <Link
            to="/app/diario"
            className={buttonVariants({
              variant: "outline",
            })}
          >
            <Book /> Diário
          </Link>
          <Link
            to="/app/perfil/$userId"
            params={{
              userId: authenticatedUser.userId.id.toString(),
            }}
            className={buttonVariants({
              variant: "outline",
            })}
          >
            <User /> Perfil
          </Link>
        </div>
      </div>

      <Jogos jogoFiltro={jogoFiltro} />
    </div>
  );
}
