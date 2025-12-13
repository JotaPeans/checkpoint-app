import { AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogTrigger,
} from "@/components/ui/dialog";
import { updateAvatar } from "@/domain/user/queries";
import { useUser } from "@/routes/app/-components/AppContenxt";
import { useMutation } from "@tanstack/react-query";
import { useRouter } from "@tanstack/react-router";
import { useState } from "react";
import { toast } from "sonner";

const EditAvatar = () => {
  const authenticatedUser = useUser();
  const router = useRouter();

  const [avatarUrl, setAvatarUrl] = useState(authenticatedUser.avatarUrl);

  const { mutate, isPending } = useMutation({
    mutationFn: async () => {
      const { data, error } = await updateAvatar({
        avatarUrl: avatarUrl,
      });
      if (error) throw error;
      return data;
    },
    onSuccess: (data) => {
      toast.success(data?.message);
      router.invalidate();
    },
    onError: (error) => {
      toast.error(error?.message);
    },
  });

  return (
    <Dialog>
      <DialogTrigger className="w-full relative">
        <>
          <AvatarImage
            src={authenticatedUser.avatarUrl}
            alt={authenticatedUser.nome}
            width={3000}
            height={3000}
            className="object-cover"
          />
          <span className="absolute w-full h-full top-0 right-0 bg-black/30 text-white flex items-center justify-center cursor-pointer rounded-full opacity-0 hover:opacity-100 transition-opacity text-center">
            Editar
          </span>
          <AvatarFallback>
            {authenticatedUser.nome.substring(0, 2)}
          </AvatarFallback>
        </>
      </DialogTrigger>
      <DialogContent>
        <div className="flex flex-col items-center gap-4">
          <AvatarImage
            src={avatarUrl}
            alt={authenticatedUser.nome}
            width={3000}
            height={3000}
            className="object-cover"
          />
          <input
            type="text"
            placeholder="URL da nova avatar"
            value={avatarUrl}
            onChange={(e) => setAvatarUrl(e.target.value)}
            className="w-full p-2 border border-zinc-700 rounded-md"
          />
        </div>
        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Cancelar</Button>
          </DialogClose>
          <Button onClick={() => mutate()} disabled={isPending}>
            {isPending ? "Salvando..." : "Salvar"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default EditAvatar;
