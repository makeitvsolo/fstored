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
    loading.current = true;
    const response = await requestFn(...args);
    loading.current = false;

    return response;
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

  const user = useUserStore((state) => state.activeUser);
  const removeUser = useUserStore((state) => state.removeActiveUser);

  const execute = async (
    ...args: Parameters<RequestFn>
  ): Promise<ReturnType<RequestFn>> => {
    loading.current = true;

    if (user === null) {
      loading.current = false;
      throw new Error("there is no active user");
    }

    const response = await requestFn(...args).catch((err) => {
      if ((err as Err).statusCode === 401) {
        removeUser();
      }

      loading.current = false;
      throw err;
    });

    loading.current = false;
    return response;
  };

  return {
    loading,
    execute,
  };
};
