import { Skeleton } from "@/components/ui/skeleton";
import {
  listComentarioByComentarioPaiId,
  listComentarioByListaId,
  listComentariosByAvaliacaoId,
} from "@/domain/comentario/queries";
import { useQuery } from "@tanstack/react-query";
import ComentarioCard from "./ComentarioCard";

interface ListComentariosProps {
  id: string | number;
  fetchBy: "avaliacao" | "lista" | "pai";
}

const fetchComentarioMapper = {
  avaliacao: listComentariosByAvaliacaoId,
  lista: listComentarioByListaId,
  pai: listComentarioByComentarioPaiId,
};

const ListComentarios = ({ fetchBy, id }: ListComentariosProps) => {
  const { data, isPending } = useQuery({
    queryKey: ["comentarios", id],
    queryFn: async () => {
      const fetchComentario = fetchComentarioMapper[fetchBy];
      const { data } = await fetchComentario(id);

      return data;
    },
  });

  return (
    <div className="flex flex-col gap-4">
      {isPending ? (
        <Skeleton />
      ) : (
        data?.map((comentario, key) => (
          <ComentarioCard comentario={comentario} key={key} />
        ))
      )}
    </div>
  );
};

export default ListComentarios;
