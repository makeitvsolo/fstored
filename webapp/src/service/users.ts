import { Err, usersApi } from "@api";
import { useRequest } from "@service";

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
