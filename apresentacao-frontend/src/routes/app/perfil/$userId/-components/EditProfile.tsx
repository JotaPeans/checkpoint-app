import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Form, FormField } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { updateProfile } from "@/domain/user/queries";
import { useUser } from "@/routes/app/-components/AppContenxt";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "@tanstack/react-router";
import { Pen, X } from "lucide-react";
import { useTransition } from "react";
import { useFieldArray, useForm } from "react-hook-form";
import { toast } from "sonner";
import z from "zod";

const profileSchema = z.object({
  nome: z.string().min(1, "Nome é obrigatório"),
  bio: z.string().min(1, "Bio é obrigatória"),
  redesSociais: z.array(
    z.object({
      plataforma: z.string(),
      username: z.string().min(1, "Username é obrigatório"),
    })
  ),
});

const EditProfile = () => {
  const currentUser = useUser();
  const router = useRouter();

  const [isPending, startTransition] = useTransition();

  const form = useForm({
    resolver: zodResolver(profileSchema),
    defaultValues: {
      nome: currentUser.nome,
      bio: currentUser.bio,
      redesSociais: currentUser.redesSociais ?? [],
    },
  });

  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: "redesSociais",
  });

  function onSubmit(data: z.infer<typeof profileSchema>) {
    startTransition(async () => {
      const { data: updatedUser, error } = await updateProfile({
        nome: data.nome,
        bio: data.bio,
        redesSociais: data.redesSociais
      });

      if (updatedUser) {
        toast.success(updatedUser.message);
        router.invalidate();
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  return (
    <Dialog>
      <DialogTrigger asChild className="ml-auto self-start">
        <Button size="icon" variant="ghost">
          <Pen />
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Editar Perfil</DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex flex-col gap-4"
          >
            <FormField
              control={form.control}
              name="nome"
              render={({ field }) => (
                <div className="grid grid-cols-2 gap-4">
                  <Label htmlFor={field.name} className="capitalize">
                    {field.name}
                  </Label>
                  <Input {...field} />
                </div>
              )}
            />
            <FormField
              control={form.control}
              name="bio"
              render={({ field }) => (
                <div className="grid grid-cols-2 gap-4">
                  <Label htmlFor={field.name} className="capitalize">
                    {field.name}
                  </Label>
                  <Textarea {...field} />
                </div>
              )}
            />
            <FormField
              control={form.control}
              name="redesSociais"
              render={({ field }) => (
                <div className="flex flex-col gap-4">
                  <Label htmlFor={field.name} className="capitalize">
                    {field.name}
                  </Label>
                  <div className="flex flex-col gap-2">
                    {fields.map((item, key: number) => (
                      <div key={item.id} className="flex items-center gap-2">
                        <FormField
                          control={form.control}
                          name={`redesSociais.${key}.plataforma`}
                          render={({ field }) => (
                            <Input {...field} placeholder="Plataforma" />
                          )}
                        />
                        <FormField
                          control={form.control}
                          name={`redesSociais.${key}.username`}
                          render={({ field }) => (
                            <Input {...field} placeholder="Username" />
                          )}
                        />
                        <Button
                          type="button"
                          size="icon"
                          onClick={() => remove(key)}
                        >
                          <X />
                        </Button>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            />
            <Button
              type="button"
              onClick={() => append({ plataforma: "", username: "" })}
            >
              Adicionar Rede Social
            </Button>
            <DialogFooter>
              <DialogClose asChild>
                <Button variant="outline">Cancelar</Button>
              </DialogClose>

              <Button type="submit" disabled={isPending}>
                {isPending ? "Salvando..." : "Salvar"}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};

export default EditProfile;
