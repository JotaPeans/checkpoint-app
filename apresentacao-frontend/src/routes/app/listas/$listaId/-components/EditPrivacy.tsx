import { Switch } from "@/components/ui/switch";
import type { ListaJogos } from "@/domain/lista/ListaJogos";
import { changePrivacidadeByListaId } from "@/domain/lista/queries";
import { useRouter } from "@tanstack/react-router";
import { useTransition } from "react";
import { toast } from "sonner";

interface EditPrivacyProps {
  lista: ListaJogos;
}

const privacyMapper = {
  true: "Sua lista está visível apenas para você",
  false: "Sua lista está visível para todos",
};

const EditPrivacy = ({ lista }: EditPrivacyProps) => {
  const router = useRouter();
  const [isLoading, startEdit] = useTransition();

  function handleSave(isPrivate: boolean) {
    startEdit(async () => {
      const { data, error } = await changePrivacidadeByListaId(lista.id.id, {
        isPrivate,
      });

      if (data) {
        toast.success(data.message);
        router.invalidate();
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  return (
    <div className="flex items-center justify-between rounded-xl border border-zinc-500 bg-zinc-800/50 p-5">
      <div className="flex flex-col">
        <p>Privacidade da Lista</p>
        <p className="text-sm text-zinc-400">
          {privacyMapper[`${lista.isPrivate}`]}
        </p>
      </div>
      <Switch
        disabled={isLoading}
        className="h-6 w-10"
        thumbClassName="size-5"
        checked={lista.isPrivate}
        onCheckedChange={(v) => handleSave(v)}
      />
    </div>
  );
};

export default EditPrivacy;
