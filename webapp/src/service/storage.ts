import { Err, filesApi, foldersApi } from "@api";
import { useFoldersStore } from "@store";

import { useMutation, useQuery } from "./common";

export interface Message {
  ok: string | null;
  error: string | null;
}

export const useOpenFolder = () => {
  const setFolder = useFoldersStore((state) => state.setFolder);
  const { loading, data, refetch } = useQuery(foldersApi.folder);

  const proxy = async (path: string) => {
    setFolder(path);
    await refetch(path);
  };

  return {
    loading,
    data,
    refetch: proxy,
  };
};

export const useCreateFolder = () => {
  const { loading, execute } = useMutation(foldersApi.make);

  const proxy = async (path: string, name: string): Promise<Message> => {
    try {
      const newFolder = path.concat(name, "/");
      await execute(newFolder);
      return {
        ok: "folder created",
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
    loading,
    execute: proxy,
  };
};

export const useRemoveFolder = () => {
  const { loading, execute } = useMutation(foldersApi.remove);

  const proxy = async (path: string): Promise<Message> => {
    try {
      await execute(path);
      return {
        ok: "folder created",
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
    loading,
    execute: proxy,
  };
};

export const useRenameFolder = () => {
  const { loading, execute } = useMutation(foldersApi.move);

  const proxy = async (path: string, name: string): Promise<Message> => {
    try {
      const oldName = path
        .split("/")
        .reduce((first, second) => (second !== "" ? second : first));

      const destination = [...path.split("/"), name]
        .filter((part) => part !== oldName && part !== "")
        .join("/");

      await execute(path, `/${destination}/`);
      return {
        ok: "folder renamed",
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
    loading,
    execute: proxy,
  };
};

export const useUploadFiles = () => {
  const { loading, execute } = useMutation(filesApi.upload);

  const proxy = async (
    path: string,
    files: File[],
    overwrite: boolean
  ): Promise<Message> => {
    try {
      await execute(path, files, overwrite);
      return {
        ok: "files uploaded",
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
    loading,
    execute: proxy,
  };
};

export const useDownloadFile = () => {
  const download = async (path: string) => {
    try {
      const filename = path.split("/").reduce((_, second) => second);
      const blob = await filesApi.download(path);

      const url = window.URL.createObjectURL(new Blob([blob]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", filename);
      document.body.appendChild(link);
      link.click();
    } catch (err) {
      console.log(err);
    }
  };

  return {
    fetch: download,
  };
};

export const useRemoveFile = () => {
  const { loading, execute } = useMutation(filesApi.remove);

  const proxy = async (path: string): Promise<Message> => {
    try {
      await execute(path);
      return {
        ok: "folder created",
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
    loading,
    execute: proxy,
  };
};
