import axios from "axios";

import { API_URL, api } from "./axios";

export type MetaData = {
  path: string;
  resource: string;
  size: number | null;
  modificationTime: Date | null;
};

export type FolderContent = {
  path: string;
  children: MetaData[];
};

export type MatchingResources = {
  objects: MetaData[];
};

export const foldersApi = {
  make: async (path: string): Promise<void> => {
    await api.post(
      `/storage/folders${path}`,

      null,

      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  },

  move: async (source: string, destination: string): Promise<void> => {
    await api.post(
      `/storage/folders${destination}`,

      null,

      {
        headers: {
          "Content-Type": "application/json",
        },
        params: { mvfrom: source },
      }
    );
  },

  remove: async (path: string): Promise<void> => {
    await api.delete(`/storage/folders${path}`);
  },

  folder: async (path: string): Promise<FolderContent> => {
    return await api.get(`/storage/folders${path}`);
  },

  search: async (resource: string): Promise<MatchingResources> => {
    return await api.get("/storage/folders", {
      params: { search: resource },
    });
  },
};

export const filesApi = {
  upload: async (
    path: string,
    files: File[],
    overwrite: boolean
  ): Promise<void> => {
    const form = new FormData();

    files.forEach((file) => {
      form.append("file", file);
    });

    if (overwrite) {
      await api.put(`/storage/files${path}`, form, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
    } else {
      await api.post(`/storage/files${path}`, form, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
    }
  },

  move: async (source: string, destination: string): Promise<void> => {
    await api.post(
      `/storage/files${destination}`,

      null,

      {
        headers: {
          "Content-Type": "application/json",
        },
        params: { mvfrom: source },
      }
    );
  },

  remove: async (path: string): Promise<void> => {
    await api.delete(`/storage/files${path}`);
  },

  download: async (path: string): Promise<Blob> => {
    return await axios
      .get(`/storage/files${path}`, {
        baseURL: API_URL,
        withCredentials: true,
        responseType: "blob",
      })
      .then((response) => response.data);
  },
};
