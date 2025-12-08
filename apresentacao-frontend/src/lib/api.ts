import axios from "axios";

export interface BasePaginatedType {
  skip: number;
  page_size: number;
}

export const api = axios.create({
  baseURL: `${import.meta.env.VITE_API_URL}/api`,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

export interface ResponseProps<T> {
  data: T | null;
  error: {
    message: string;
  } | null;
}

export type MessageType = {
  message: string;
}

export async function apiWrapper<T>(
  callback: () => Promise<T>,
  caller: string,
): Promise<ResponseProps<T>> {
  try {
    const returnData = await callback();

    return {
      data: returnData,
      error: null,
    };
  } catch (error: any) {
    let message = error.response?.data.error || "";

    console.log(`🚀 ~ ${caller} error:`, message);

    return {
      data: null,
      error: {
        message: message,
      },
    };
  }
}