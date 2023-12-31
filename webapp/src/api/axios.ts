import axios, { AxiosError, AxiosResponse } from "axios";

export const API_URL = import.meta.env.VITE_API_URL;

export interface Ok<T> {
  status_code: number;
  status: string;
  data: T;
}

export interface Err {
  status_code: number;
  error: string;
  details: string;
}

export const api = axios.create({
  baseURL: API_URL,
  withCredentials: true,
});

api.interceptors.response.use(
  (ok: AxiosResponse) => {
    if (ok.data.data) {
      return ok.data.data;
    }

    return {};
  },

  (err: AxiosError<Err> | Error) => {
    if (axios.isAxiosError<Err>(err) && err.response) {
      throw err.response.data;
    }

    console.log(err);
    throw err;
  }
);
