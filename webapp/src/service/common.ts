import { useState } from "react";

import { Err } from "@api";
import { useUserStore } from "@store";

export const useQuery = <QueryFn extends (...args: any[]) => any>(
  fn: QueryFn
) => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<Awaited<ReturnType<QueryFn>> | null>(null);

  const removeUser = useUserStore((state) => state.removeActiveUser);

  const proxy = async (...args: Parameters<QueryFn>) => {
    try {
      setLoading(true);

      const response = await fn(...args);
      setData(response);

      setLoading(false);
    } catch (err) {
      if ((err as Err).status_code === 401) {
        removeUser();
      }

      setLoading(false);
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
  const [loading, setLoading] = useState(false);

  const removeUser = useUserStore((state) => state.removeActiveUser);

  const proxy = async (...args: Parameters<MutationFn>): Promise<ReturnType<MutationFn>> => {
    try {
      setLoading(true);

      const response = await fn(...args);

      setLoading(false);
      return response;
    } catch (err) {
      if ((err as Err).status_code === 401) {
        removeUser();
      }

      setLoading(false);
      throw err;
    }
  };

  return {
    loading,
    execute: proxy,
  };
};
