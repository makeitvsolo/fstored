import { Err, filesApi, foldersApi } from "@api";
import { useFoldersStore } from "@store";

import { useMutation, useQuery } from "./common";

export interface Message {
  ok: string | null;
  error: string | null;
}

export const useCurrentFolder = () => {
  const currentFolder = useFoldersStore((state) => state.folder);
  const { loading, data, refetch } = useQuery(foldersApi.folder);

  const proxy = async () => {
    await refetch(currentFolder);
  };

  return {
    loading,
    data,
    refetch: proxy,
  };
};

export const useCreateFolder = () => {
  const currentFolder = useFoldersStore((state) => state.folder);
  const { loading, execute } = useMutation(foldersApi.make);

  const proxy = async (name: string): Promise<Message> => {
    try {
      const newFolder = currentFolder.concat(name, "/");
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

export const useUploadFiles = () => {
  const currentFolder = useFoldersStore((state) => state.folder);
  const { loading, execute } = useMutation(filesApi.upload);

  const proxy = async (
    files: File[],
    overwrite: boolean
  ): Promise<Message> => {
    try {
      await execute(currentFolder, files, overwrite);
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
