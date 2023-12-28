import { useRef } from "react";

import { Err } from "@api";
import { useUserStore } from "@store";

export const useRequest = <RequestFn extends (...args: any[]) => Promise<any>>(
  requestFn: RequestFn
) => {
  const loading = useRef(false);

  const execute = async (
    ...args: Parameters<RequestFn>
  ): Promise<ReturnType<RequestFn>> => {
    try {
      loading.current = true;
      const response = await requestFn(...args);
      loading.current = false;
      return response;
    } catch (err) {
      loading.current = false;
      throw err;
    }
  };

  return {
    loading,
    execute,
  };
};

export const useAuthenticatedRequest = <
  RequestFn extends (...args: any[]) => Promise<any>
>(
  requestFn: RequestFn
) => {
  const loading = useRef(false);

  const removeUser = useUserStore((state) => state.removeActiveUser);

  const execute = async (
    ...args: Parameters<RequestFn>
  ): Promise<ReturnType<RequestFn>> => {
    try {
      loading.current = true;

      const response = await requestFn(...args);

      loading.current = false;
      return response;
    } catch (err) {
      if ((err as Err).statusCode === 401) {
        removeUser();
      }

      loading.current = false;
      throw err;
    }
  };

  return {
    loading,
    execute,
  };
};
