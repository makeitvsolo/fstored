import axios, { AxiosError, AxiosResponse } from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export interface Ok<T> {
  statusCode: number;
  status: string;
  data: T;
}

export interface Err {
  statusCode: number;
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
