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

export type StorageErrorMessage = {
  code: string | null;
  message: string;
};

export const foldersApi = {
  make: async (path: string): Promise<void> => {
    await api.post(
      `/storage/folders${path}`,

      {},

      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  },

  move: async (source: string, destination: string): Promise<void> => {
    await api.post(
      `/storage/folders${destination}?mvfrom=${source}`,

      {},

      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  },

  remove: async (path: string): Promise<void> => {
    await api.delete(
      `/storage/folders${path}`,

      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  },

  root: async (): Promise<FolderContent> => {
    return await api.get("/storage/folders", {
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

  folder: async (path: string): Promise<FolderContent> => {
    return await api.get(`/storage/folders${path}`, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

  fullSearch: async (resource: string): Promise<MatchingResources> => {
    return await api.get(`/storage/folders?search=${resource}`, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

  relativeSearch: async (
    path: string,
    resource: string
  ): Promise<MatchingResources> => {
    return await api.get(`/storage/folders${path}?search=${resource}`, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  },
};

export const filesApi = {
  upload: async (files: FormData): Promise<void> => {
    await api.post("/storage/files", files, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  uploadToFolder: async (path: string, files: FormData): Promise<void> => {
    await api.post(`/storage/files${path}`, files, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  move: async (source: string, destination: string): Promise<void> => {
    await api.post(
      `/storage/files${destination}?mvfrom=${source}`,

      {},

      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  },

  overwrite: async (files: FormData): Promise<void> => {
    await api.put("/storage/files", files, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  overwriteToFolder: async (path: string, files: FormData): Promise<void> => {
    await api.put(`/storage/files${path}`, files, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  remove: async (path: string): Promise<void> => {
    await api.delete(`/storage/files${path}`, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

  download: async (path: string): Promise<void> => {
    await api.get(`/storage/files${path}`, {
      headers: {
        "Content-Type": "application/json",
      },
      responseType: "blob",
    });
  },
};
