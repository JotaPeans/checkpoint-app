import {
  createFileRoute,
  Link,
  redirect,
  useNavigate,
} from "@tanstack/react-router";
import { motion } from "motion/react";
import { useEffect, useState, useTransition } from "react";
import { ChevronLeft } from "lucide-react";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";

import FormInputField from "@/components/FormInputField";
import { Form, FormField } from "@/components/ui/form";
import { Button, buttonVariants } from "@/components/ui/button";
import { login } from "@/domain/autenticacao/queries";
import { getMe } from "@/domain/user/queries";
import { cn } from "@/lib/utils";

export const Route = createFileRoute("/_auth/login/")({
  component: RouteComponent,
  beforeLoad: async () => {
    const { data: user } = await getMe();

    if (user) {
      throw redirect({ to: "/app" });
    }
  },
});

const formSchema = z.object({
  email: z.email({
    message: "O email é obrigatório",
  }),
  password: z.string({
    message: "A senha é obrigatória",
  }),
});

type FormSchemaType = z.infer<typeof formSchema>;

function RouteComponent() {
  const navigate = useNavigate();

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
      email: "",
      password: "",
    },
  });

  function onSubmit(values: FormSchemaType) {
    startLogin(async () => {
      const { data, error } = await login({
        email: values.email,
        senha: values.password,
      });

      if (data) {
        navigate({
          to: "/app",
        });
      } else if (error) {
        toast.error(error.message);
      }
    });
  }

  return (
    <main className="flex-1 relative flex">
      <motion.div
        className="flex-1 bg-black hidden lg:flex p-28 relative"
        initial={{ x: 400 }}
        animate={{ x: 0 }}
      >
        <Link
          to="/"
          className="text-zinc-100 flex items-center justify-center transition-all rounded-lg hover:bg-mediumBlue size-10"
        >
          <ChevronLeft size={24} />
        </Link>
        <motion.h3
          className="w-96 text-wrap text-6xl font-bricolage text-white font-semibold mt-auto z-10"
          initial={{ y: -100, scale: 0, opacity: 0 }}
          animate={{
            y: 0,
            scale: 1,
            opacity: 1,
            transition: { delay: 0.3, duration: 0.5, type: "spring" },
          }}
        >
          Bem-Vindo <span className="text-5xl">de volta!</span>
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
            <h5 className="text-start font-semibold text-xl">Login</h5>
            <p className="text-principal">
              Bem vindo de volta! Faça login para acessar sua conta.
            </p>

            <div className="flex flex-col gap-4 mt-2">
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormInputField
                    {...field}
                    label="Email"
                    required
                    placeholder="professor@school.com"
                    classNames={{
                      labelClassName: "text-principal",
                    }}
                  />
                )}
              />
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormInputField
                    {...field}
                    type="password"
                    label="Senha"
                    required
                    placeholder="***"
                  />
                )}
              />
            </div>

            <Button size="lg" isLoading={isLoading} className="mt-4">
              Entrar
            </Button>

            <p className="w-full text-start mt-2 text-principal">
              Ainda não possui uma conta?{" "}
              <Link
                to="/cadastro"
                className={buttonVariants({
                  variant: "link",
                  className: "p-0",
                })}
              >
                Cadastre-se!
              </Link>
            </p>
          </form>
        </Form>
      </motion.div>
    </main>
  );
}
