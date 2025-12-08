import { getMe } from "@/domain/user/queries";
import {
  createFileRoute,
  Outlet,
  redirect,
  useLocation,
  useNavigate,
} from "@tanstack/react-router";
import AppProvider from "./-components/AppContenxt";
import { useEffect } from "react";

export const Route = createFileRoute("/app")({
  component: RouteComponent,
  beforeLoad: async () => {
    const { data: user } = await getMe();

    if (!user) {
      throw redirect({ to: "/login" });
    }
  },
  loader: async () => {
    const { data: user } = await getMe();

    return user;
  },
});

function RouteComponent() {
  const user = Route.useLoaderData();
  const location = useLocation();
  const navigate = useNavigate();

  const pathname = location.pathname;

  useEffect(() => {
    if (pathname === "/app") {
      navigate({ to: "/app/jogos" });
    }
  }, []);

  return (
    <AppProvider user={user!}>
      <Outlet />
    </AppProvider>
  );
}
