import { ChevronLeft } from "lucide-react";
import { Link, createFileRoute, useNavigate } from "@tanstack/react-router";

import { buttonVariants } from "@/components/ui/button";
import { getDiario } from "@/domain/diario/queries";

import AddDiarioRegistroDialog from "./-components/AddDiarioRegistroDialog";
import RegistroCard from "./-components/RegistroCard";

export const Route = createFileRoute("/app/diario/")({
  component: RouteComponent,
  loader: async () => {
    const { data } = await getDiario();
    return data;
  },
});

function RouteComponent() {
  const navigate = useNavigate();
  const diario = Route.useLoaderData();

  if (!diario) {
    navigate({
      to: "/app/jogos",
    });
    return null;
  }

  const headerText =
    diario.registros.length === 1 ? "jogo registrado" : "jogos registrados";

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
          <div className="flex flex-col">
            <h1 className="text-xl font-semibold">Meu Diário de Jogos</h1>
            <p className="text-sm text-zinc-500">
              {diario.registros.length} {headerText}
            </p>
          </div>
        </div>

        <div className="flex gap-2 items-center">
          <img src="/logo.png" alt="Checkpoint" width={40} height={93} />
          <h1 className="text-xl font-bold">Checkpoint</h1>
        </div>
      </div>

      <div className="flex-1 flex flex-col gap-5 2xl:px-32 mt-5">
        <div className="flex items-center justify-between gap-4">
          <h3 className="text-xl">Meus Registros</h3>
          <AddDiarioRegistroDialog
            jogosRegistrados={diario.registros.map(
              (registro) => registro.jogoId
            )}
          />
        </div>

        <div className="flex flex-col gap-4">
          {diario.registros.map((registro) => (
            <RegistroCard key={registro.id.id} registro={registro} />
          ))}
        </div>
      </div>
    </div>
  );
}
