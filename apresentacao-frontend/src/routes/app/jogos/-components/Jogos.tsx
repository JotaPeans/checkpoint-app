import { useQuery } from "@tanstack/react-query";
import { motion } from "motion/react";

import { Skeleton } from "@/components/ui/skeleton";
import type { Jogo } from "@/domain/jogo/Jogo";
import { listJogos, listTopTagsByJogoId } from "@/domain/jogo/queries";
import { Link } from "@tanstack/react-router";
import { ChevronRight } from "lucide-react";
import { buttonVariants } from "@/components/ui/button";
import SuggestNewTag from "./SuggestNewTag";

interface JogoCardProps {
  jogo: Jogo;
}

const JogoCard = ({ jogo }: JogoCardProps) => {
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

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{
        opacity: 1,
        transition: {
          type: "spring",
          duration: 1,
        },
      }}
      className="bg-zinc-800 w-full h-full rounded-xl flex flex-col overflow-hidden hover:scale-105 transition-all row-span-1 max-h-96"
    >
      <img src={jogo.capaUrl} className="max-h-52 h-52 object-cover" />
      <div className="p-4 flex flex-col gap-2 flex-1">
        <p>{jogo.nome}</p>
        <span className="text-sm font-medium text-zinc-500">
          {jogo.informacaoTitulo}
        </span>

        <div className="flex items-center gap-4 flex-1 justify-between mb-1.5">
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
            <span className="h-full w-10 absolute -right-8 top-0 bg-zinc-800 blur-xs" />
          </div>
          <SuggestNewTag jogo={jogo} refetch={refetch} />
        </div>

        <div className="flex items-center gap-4 mt-auto">
          <Link
            to="/app/jogos/$jogoId"
            params={{
              jogoId: jogo.id.id.toString(),
            }}
            className={buttonVariants({
              className: "w-full",
            })}
          >
            Ver detalhes <ChevronRight />
          </Link>
        </div>
      </div>
    </motion.div>
  );
};

interface JogosProps {
  jogoFiltro?: string;
  filterByIds?: number[];
}

const Jogos = ({ jogoFiltro, filterByIds }: JogosProps) => {
  const { isPending: jogosIsPending, data: jogos } = useQuery({
    queryKey: ["jogos"],
    queryFn: async () => {
      const { data } = await listJogos();

      return data;
    },
  });

  const filteredJogos = jogos?.filter((jogo) => {
    if (jogoFiltro) {
      return jogo.nome.toLowerCase().includes(jogoFiltro.toLowerCase());
    }
    if (filterByIds) {
      return filterByIds.includes(jogo.id.id);
    }
    return jogo;
  });

  if (jogosIsPending && !jogos) {
    return (
      <>
        <Skeleton />
        <Skeleton />
        <Skeleton />
      </>
    );
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 2xl:grid-cols-6 grid-rows-1 gap-4">
      {filteredJogos?.map((jogo, key) => (
        <JogoCard key={key} jogo={jogo} />
      ))}
    </div>
  );
};

export default Jogos;
