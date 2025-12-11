"use client";

import { ChevronLeft, ChevronRight } from "lucide-react";
import { type Dispatch, type SetStateAction, useCallback } from "react";

interface PaginatorProps {
  totalPages: number;
  currentPage: number;
  setCurrentPage: Dispatch<SetStateAction<number>>;
  hasNext?: boolean;
  hasPrevious?: boolean;
}
const Paginator = ({
  totalPages,
  currentPage,
  setCurrentPage,
  hasNext = true,
  hasPrevious = true,
}: PaginatorProps) => {
  const nextPage = useCallback(() => {
    setCurrentPage((prev) => ++prev);
  }, [currentPage]);

  const handlePageChange = useCallback((page: number) => {
    setCurrentPage(page);
  }, []);

  const prevPage = useCallback(() => {
    setCurrentPage((prev) => --prev);
  }, [currentPage]);

  const getPaginationItems = () => {
    if (isNaN(totalPages)) return [];

    const paginationItems = [];
    const maxButtons = 4; // Total de botões desejados
    const middleButtons = 2; // Número de botões do meio

    // Condição para exibir todas as páginas se o total for menor ou igual a maxButtons
    if (totalPages <= maxButtons) {
      for (let i = 1; i <= totalPages; i++) {
        paginationItems.push(i);
      }
      return paginationItems;
    }

    // Adiciona a primeira página
    // paginationItems.push(1);

    // Define as páginas do meio
    let startPage = Math.max(2, currentPage - Math.floor(middleButtons / 2));
    let endPage = startPage + middleButtons - 1;

    // Ajuste para não exceder o total de páginas
    if (endPage >= totalPages) {
      endPage = totalPages - 1;
      startPage = Math.max(2, endPage - middleButtons + 1);
    }

    if (startPage > 2) {
      paginationItems.unshift("...");
    }

    paginationItems.unshift(1);

    // Adiciona as páginas do meio ao array
    for (let i = startPage; i <= endPage; i++) {
      paginationItems.push(i);
    }

    // Adiciona "..." se necessário
    if (endPage < totalPages - 1) {
      paginationItems.push("...");
    }

    // Adiciona a última página
    paginationItems.push(totalPages);

    return paginationItems;
  };

  return (
    <nav className="flex items-center gap-x-1" aria-label="Pagination">
      <button
        type="button"
        className="min-h-[38px] min-w-[38px] py-2 px-2.5 inline-flex justify-center items-center gap-x-2 text-sm rounded-lg border border-transparent text-gray-800 hover:bg-gray-100 focus:outline-none  disabled:opacity-50 disabled:pointer-events-none dark:border-transparent dark:text-white dark:hover:bg-white/10"
        aria-label="Previous"
        onClick={prevPage}
        disabled={!hasPrevious || currentPage === 1}
      >
        <ChevronLeft size={16} />
        <span className="sr-only">Previous</span>
      </button>
      <div className="flex items-center gap-x-1 relative">
        {getPaginationItems().map((item, key) => (
          <button
            key={key}
            type="button"
            id={`page-button-${item}`}
            data-active={item === currentPage}
            className="min-h-[38px] min-w-[38px] data-[active=true]:border data-[active=true]:border-gray-300 flex justify-center items-center text-gray-800 py-2 px-3 text-sm rounded-lg focus:outline-none focus:bg-gray-50 disabled:opacity-50 disabled:pointer-events-none dark:border-neutral-700 dark:text-white dark:focus:bg-white/10"
            aria-current="page"
            disabled={item === currentPage}
            onClick={() => {
              if (typeof item === "number") {
                handlePageChange(item);
              }
            }}
          >
            {item}
          </button>
        ))}
      </div>
      <button
        type="button"
        className="min-h-[38px] min-w-[38px] py-2 px-2.5 inline-flex justify-center items-center gap-x-2 text-sm rounded-lg border border-transparent text-gray-800 hover:bg-gray-100 focus:outline-none disabled:opacity-50 disabled:pointer-events-none dark:border-transparent dark:text-white dark:hover:bg-white/10"
        aria-label="Next"
        onClick={nextPage}
        disabled={
          !hasNext ||
          currentPage === totalPages ||
          totalPages === 0 ||
          isNaN(totalPages)
        }
      >
        <ChevronRight size={16} />
        <span className="sr-only">Next</span>
      </button>
    </nav>
  );
};

export default Paginator;
