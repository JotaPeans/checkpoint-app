import { Skeleton } from "@/components/ui/skeleton";
import {
  getListasPublicasPaginated,
  listMyListas,
} from "@/domain/lista/queries";
import { useQuery } from "@tanstack/react-query";
import ListaCard from "./ListaCard";
import Paginator from "@/components/Paginator";
import { useState } from "react";
import type { ListaJogos } from "@/domain/lista/ListaJogos";
import type { PaginatedResponse } from "@/domain/generalTypes";

interface ListasProps {
  filterPublic?: boolean;
}

const Listas = ({ filterPublic }: ListasProps) => {
  const [currentPage, setCurrentPage] = useState(1);

  const {
    isPending: isListasPending,
    data: listas,
    refetch,
  } = useQuery({
    queryKey: ["listas", `listas-${filterPublic}`, currentPage],
    queryFn: async () => {
      if (filterPublic) {
        const { data } = await getListasPublicasPaginated({
          size: 2,
          page: currentPage,
        });

        return data;
      } else {
        const { data } = await listMyListas();

        return data;
      }
    },
  });

  return (
    <>
      <div className="grid grid-cols-3 gap-4">
        {isListasPending ? (
          <Skeleton />
        ) : filterPublic ? (
          (
            listas as PaginatedResponse<ListaJogos> | null | undefined
          )?.data.map((lista, key) => (
            <ListaCard key={key} lista={lista} refetchListas={refetch} />
          ))
        ) : (
          (listas as ListaJogos[] | null | undefined)?.map((lista, key) => (
            <ListaCard key={key} lista={lista} refetchListas={refetch} />
          ))
        )}
      </div>

      {filterPublic && (
        <div className="w-full flex items-center justify-end">
          <Paginator
            currentPage={currentPage}
            setCurrentPage={setCurrentPage}
            totalPages={
              (listas as PaginatedResponse<ListaJogos> | null | undefined)
                ?.pagination.totalPages ?? 1
            }
          />
        </div>
      )}
    </>
  );
};

export default Listas;
