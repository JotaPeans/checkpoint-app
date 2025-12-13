import { Button } from "@/components/ui/button";
import { logout } from "@/domain/autenticacao/queries";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { LogOutIcon } from "lucide-react";

const Logout = () => {
  const navigate = useNavigate();

  const { mutate, isPending } = useMutation({
    mutationFn: async () => {
      await logout();

      navigate({
        to: "/login",
      });
    },
  });

  return (
    <Button variant="outline" onClick={() => mutate()} isLoading={isPending}>
      Logout <LogOutIcon className="w-5 h-5" />
    </Button>
  );
};

export default Logout;
