import { api } from "./axios";

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
