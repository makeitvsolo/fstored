import axios from "axios";

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
