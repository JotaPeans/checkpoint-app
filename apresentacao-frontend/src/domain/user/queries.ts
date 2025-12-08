import { api, apiWrapper } from "@/lib/api";
import type { User } from "./User";

export const getMe = async () => {
  return await apiWrapper(async () => {
    const { data } = await api.get<User>("/user/me");
    return data;
  }, "me");
};
