import { Err, usersApi } from "@api";
import { useAuthenticatedRequest, useRequest } from "@service";
import { useUserStore } from "@store";

export interface Message {
  ok: string | null;
  error: string | null;
}

export const useSignUp = () => {
  const { loading, execute } = useRequest(usersApi.singUp);

  const proxy = async (name: string, password: string): Promise<Message> => {
    try {
      await execute(name, password);
      return {
        ok: "user created",
        error: null,
      } as Message;
    } catch (err) {
      if ((err as Err).details) {
        return {
          ok: null,
          error: (err as Err).details,
        } as Message;
      }

      return {
        ok: null,
        error: "unexpected error",
      } as Message;
    }
  };

  return {
    loading: loading,
    execute: proxy,
  };
};

export const useSignIn = () => {
  const { loading, execute } = useRequest(usersApi.signIn);
  const setUser = useUserStore((state) => state.setActiveUser);

  const proxy = async (name: string, password: string): Promise<Message> => {
    try {
      const token = await execute(name, password);
      setUser(token.active);

      return {
        ok: "user signed",
        error: null,
      } as Message;
    } catch (err) {
      if ((err as Err).details) {
        return {
          ok: null,
          error: (err as Err).details,
        } as Message;
      }

      return {
        ok: null,
        error: "unexpected error",
      } as Message;
    }
  };

  return {
    loading: loading,
    execute: proxy,
  };
};

export const useSignOut = () => {
  const { loading, execute } = useAuthenticatedRequest(usersApi.signOut);
  const removeUser = useUserStore((state) => state.removeActiveUser);

  const proxy = async () => {
    try {
      await execute();
      removeUser();

      return {
        ok: "user unsigned",
        error: null,
      } as Message;
    } catch (err) {
      if ((err as Err).details) {
        return {
          ok: null,
          error: (err as Err).details,
        } as Message;
      }

      return {
        ok: null,
        error: "unexpected error",
      } as Message;
    }
  };

  return {
    loading: loading,
    execute: proxy,
  };
};
