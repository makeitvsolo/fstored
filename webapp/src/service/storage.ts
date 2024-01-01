import { useFoldersStore } from "@store";
import { useAuthenticatedRequest } from "./common";
import { FolderContent, foldersApi } from "@api";

export const useCurrentFolder = () => {
  const path = useFoldersStore((state) => state.path);
  const { loading, execute } = useAuthenticatedRequest(foldersApi.folder);

  const proxy = async (): Promise<FolderContent> => {
    return await execute(path);
  };

  return {
    loading: loading,
    execute: proxy,
  };
};
