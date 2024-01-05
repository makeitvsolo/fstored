import { api } from "./axios";

export interface Credentials {
  name: string;
  password: string;
}

export type ActiveUser = {
  id: string;
  name: string;
};

export type AccessToken = {
  token: string;
  expiresAt: Date;
  active: ActiveUser;
};

export const usersApi = {
  singUp: async (name: string, password: string): Promise<void> => {
    await api.post(
      "/user-access/sign-up",

      {
        name: name,
        password: password,
      } as Credentials,

      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  },

  signIn: async (name: string, password: string): Promise<AccessToken> => {
    return await api.post(
      "/user-access/sign-in",

      {
        name: name,
        password: password,
      } as Credentials,

      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  },

  signOut: async (): Promise<void> => {
    await api.post(
      "/user-access/sign-out",

      null,

      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  },
};
