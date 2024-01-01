import { useRef } from "react";

import { Err } from "@api";
import { useUserStore } from "@store";

export const useQuery = <QueryFn extends (...args: any[]) => any>(
  fn: QueryFn
) => {
  const loading = useRef(false);
  const data = useRef<Awaited<ReturnType<typeof fn>>["data"] | null>(null);

  const removeUser = useUserStore((state) => state.removeActiveUser);

  const proxy = async (...args: Parameters<QueryFn>) => {
    try {
      loading.current = true;

      const response = await fn(args);
      data.current = response;

      loading.current = false;
    } catch (err) {
      if ((err as Err).status_code === 401) {
        removeUser();
      }

      loading.current = false;
      throw err;
    }
  };

  return {
    loading,
    data,
    refetch: proxy,
  };
};

export const useMutation = <MutationFn extends (...args: any[]) => any>(
  fn: MutationFn
) => {
  const loading = useRef(false);

  const removeUser = useUserStore((state) => state.removeActiveUser);

  const proxy = async (...args: Parameters<MutationFn>): Promise<ReturnType<MutationFn>> => {
    try {
      loading.current = true;

      const response = await fn(args);

      loading.current = false;
      return response;
    } catch (err) {
      if ((err as Err).status_code === 401) {
        removeUser();
      }

      loading.current = false;
      throw err;
    }
  };

  return {
    loading,
    execute: proxy,
  };
};
