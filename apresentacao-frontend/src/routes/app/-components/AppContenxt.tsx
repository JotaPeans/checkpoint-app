import type { User } from "@/domain/user/User";
import { createContext, useContext, type ReactNode } from "react";

export const AppContext = createContext<{ user: User }>({
  user: {} as any,
});

interface AppProviderProps {
  children: ReactNode;
  user: User;
}
const AppProvider = ({ children, user }: AppProviderProps) => {
  return <AppContext.Provider value={{ user }}>{children}</AppContext.Provider>;
};

export default AppProvider;

export const useUser = () => {
  const { user } = useContext(AppContext);
  return user;
}
