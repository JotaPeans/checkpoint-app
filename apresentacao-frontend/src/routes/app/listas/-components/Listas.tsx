import { Skeleton } from "@/components/ui/skeleton";
import { getListasPublicas, listMyListas } from "@/domain/lista/queries";
import { useQuery } from "@tanstack/react-query";
import ListaCard from "./ListaCard";

interface ListasProps {
  filterPublic?: boolean;
}

const Listas = ({ filterPublic }: ListasProps) => {
  const {
    isPending: isListasPending,
    data: listas,
    refetch,
  } = useQuery({
    queryKey: ["listas", `listas-${filterPublic}`],
    queryFn: async () => {
      if (filterPublic) {
        const { data } = await getListasPublicas();

        return data;
      } else {
        const { data } = await listMyListas();

        return data;
      }
    },
  });

  return (
    <div className="grid grid-cols-3">
      {isListasPending ? (
        <Skeleton />
      ) : (
        listas?.map((lista, key) => (
          <ListaCard key={key} lista={lista} refetchListas={refetch} />
        ))
      )}
    </div>
  );
};

export default Listas;
