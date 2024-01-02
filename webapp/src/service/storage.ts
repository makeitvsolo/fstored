import { foldersApi } from "@api";

import { useQuery } from "./common";
import { useFoldersStore } from "@store";

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
