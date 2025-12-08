import { createFileRoute, Link } from "@tanstack/react-router";
import { ChevronLeft, Search } from "lucide-react";
import { useState } from "react";

import { buttonVariants } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

import CreateNewLista from "./-components/CreateNewLista";
import Listas from "./-components/Listas";

export const Route = createFileRoute("/app/listas/")({
  component: RouteComponent,
});

function RouteComponent() {
  const [listaFiltro, setListaFiltro] = useState("");

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
          <h1 className="text-xl font-semibold">Minhas Listas</h1>
        </div>

        <div className="flex gap-2 items-center">
          <img src="/logo.png" alt="Checkpoint" width={40} height={93} />
          <h1 className="text-xl font-bold">Checkpoint</h1>
        </div>
      </div>

      <div className="flex-1 flex flex-col gap-5 2xl:px-32 mt-5">
        <div className="flex items-center gap-4">
          <div className="relative flex items-center w-full">
            <Search className="absolute left-2 text-zinc-500" size={22} />
            <Input
              className="pl-9 w-full"
              value={listaFiltro}
              onChange={(e) => setListaFiltro(e.target.value)}
              placeholder="Pesquisar jogo"
            />
          </div>

          <CreateNewLista />
        </div>

        <h3 className="text-xl pb-3 border-b">Minhas Listas</h3>

        <Listas />

        <h3 className="text-xl pb-3 border-b mt-10">Listas Públicas</h3>

        <Listas filterPublic />
      </div>
    </div>
  );
}
