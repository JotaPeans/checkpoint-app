import { createFileRoute, Link, redirect } from "@tanstack/react-router";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useEffect, useState, useTransition } from "react";
import { motion } from "motion/react";
import { toast } from "sonner";
import { z } from "zod";

import { Button, buttonVariants } from "@/components/ui/button";
import { Form, FormField } from "@/components/ui/form";
import FormInputField from "@/components/FormInputField";
import { cadastrar } from "@/domain/autenticacao/queries";
import { getMe } from "@/domain/user/queries";
import { cn } from "@/lib/utils";

export const Route = createFileRoute("/_auth/cadastro/")({
  component: RouteComponent,
  beforeLoad: async () => {
    const { data: user } = await getMe();

    if (user) {
      return redirect({ to: "/app" });
    }
  },
});

const formSchema = z
  .object({
    nome: z.string({
      message: "O nome é obrigatório",
    }),
    email: z
      .string({
        message: "O email é obrigatório",
      })
      .email({
        message: "Insira um email válido",
      }),
    senha: z.string({
      message: "A senha é obrigatória",
    }),
    confirmPassword: z.string({
      message: "A confirmação da senha é obrigatória",
    }),
  })
  .refine((data) => data.senha === data.confirmPassword, {
    message: "As senhas não coincidem!",
    path: ["confirmPassword"],
  });

type FormSchemaType = z.infer<typeof formSchema>;

function RouteComponent() {
  const [isLoading, startLogin] = useTransition();

  const [isPatternVisible, setIsPatternVisible] = useState(false);

  useEffect(() => {
    setTimeout(() => {
      setIsPatternVisible(true);
    }, 1000);
  }, []);

  const form = useForm<FormSchemaType>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      nome: "",
      email: "",
      senha: "",
      confirmPassword: "",
    },
  });

  function onSubmit(values: FormSchemaType) {
    startLogin(async () => {
      const { confirmPassword, ...body } = values;
      const { data, error } = await cadastrar(body);

      if (data) {
        toast.success(data.message);
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  return (
    <main className="flex-1 relative flex bg-light_minus_1">
      <motion.div
        initial={{ scale: 0, opacity: 0 }}
        animate={{
          scale: 1,
          opacity: 1,
          transition: {
            type: "spring",
            delay: 0.5,
            duration: 0.6,
          },
        }}
        className="flex flex-col items-center justify-center gap-10 flex-1 max-w-[725px] px-4 py-10"
      >
        <div className="flex flex-col gap-4 items-center">
          <img src="/logo.png" alt="Checkpoint" width={100} height={93} />
          <h1 className="text-3xl font-bold">Checkpoint</h1>
        </div>

        <Form {...form}>
          <form
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex flex-col gap-2"
          >
            <h5 className="text-start font-semibold text-xl">Cadastro</h5>
            <p className="text-principal">
              Insira suas informações para se cadastrar na plataforma!
            </p>

            <div className="flex flex-col gap-4 mt-4">
              <FormField
                control={form.control}
                name="nome"
                render={({ field }) => (
                  <FormInputField
                    {...field}
                    label="Nome"
                    required
                    placeholder="Insira seu nome"
                    classNames={{
                      labelClassName: "text-principal",
                    }}
                  />
                )}
              />

              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormInputField
                    {...field}
                    label="Email"
                    required
                    placeholder="Insira seu email"
                    classNames={{
                      labelClassName: "text-principal",
                    }}
                  />
                )}
              />

              <div className="flex items-center flex-wrap mobile:flex-nowrap justify-between gap-5">
                <FormField
                  control={form.control}
                  name="senha"
                  render={({ field }) => (
                    <FormInputField
                      {...field}
                      label="Senha"
                      type="password"
                      required
                      placeholder="Insira sua senha"
                      classNames={{
                        rootClassName: "w-full",
                        labelClassName: "text-principal",
                      }}
                    />
                  )}
                />
                <FormField
                  control={form.control}
                  name="confirmPassword"
                  render={({ field }) => (
                    <FormInputField
                      {...field}
                      label="Confirma sua senha"
                      type="password"
                      required
                      placeholder="confirme sua senha"
                      classNames={{
                        rootClassName: "w-full",
                        labelClassName: "text-principal",
                      }}
                    />
                  )}
                />
              </div>

              <Button isLoading={isLoading} size="lg" className="mt-2">
                Cadastrar
              </Button>
            </div>

            <p className="w-full text-start mt-2 text-principal">
              Já possui uma conta?{" "}
              <Link
                to="/login"
                className={buttonVariants({
                  variant: "link",
                  className: "p-0 px-0",
                })}
              >
                Entre Aqui!
              </Link>
            </p>
          </form>
        </Form>
      </motion.div>

      <motion.div
        className="flex-1 bg-black hidden lg:flex p-28 relative"
        initial={{ x: -400 }}
        animate={{ x: 0 }}
      >
        <motion.h3
          className="w-72 flex flex-wrap text-6xl font-bricolage text-white font-semibold mt-auto z-10"
          initial={{ y: -100, scale: 0, opacity: 0 }}
          animate={{
            y: 0,
            scale: 1,
            opacity: 1,
            transition: { delay: 0.3, duration: 0.5, type: "spring" },
          }}
        >
          Olá, <span className="text-5xl">tudo bem?</span>
        </motion.h3>

        <div className="w-full h-full top-0 left-0 absolute pointer-events-none">
          <video
            src="https://www.pexels.com/download/video/28561361"
            autoPlay
            loop
            muted
            className={cn(
              "w-full h-full object-cover transition-all duration-1000",
              isPatternVisible ? "opacity-20" : "opacity-0"
            )}
          />
        </div>
      </motion.div>
    </main>
  );
}
