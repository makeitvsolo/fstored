import { Box } from "@chakra-ui/react";

import {
  useCreateFolder,
  useDownloadFile,
  useOpenFolder,
  useRemoveFile,
  useRemoveFolder,
  useRenameFile,
  useRenameFolder,
  useUploadFiles,
} from "@service";
import { useFoldersStore } from "@store";

import { Bar } from "./Bar";
import { Workspace } from "./Workspace";

export const Storage = () => {
  const folder = useFoldersStore((state) => state.folder);

  const openFolder = useOpenFolder();
  const createFolder = useCreateFolder();
  const removeFolder = useRemoveFolder();
  const renameFolder = useRenameFolder();

  const uploadFiles = useUploadFiles();
  const downloadFile = useDownloadFile();
  const removeFile = useRemoveFile();
  const renameFile = useRenameFile();

  const onCreateFolder = async (path: string, name: string) => {
    const response = await createFolder.execute(path, name);
    await openFolder.refetch(path);
    return response;
  };

  const onRemoveFolder = async (path: string) => {
    const response = await removeFolder.execute(path);
    await openFolder.refetch(folder);
    return response;
  };

  const onRenameFolder = async (path: string, name: string) => {
    const response = await renameFolder.execute(path, name);

    if (response.ok) {
      await openFolder.refetch(folder);
    }

    return response;
  };

  const onRenameFile = async (path: string, name: string) => {
    const response = await renameFile.execute(path, name);

    if (response.ok) {
      await openFolder.refetch(folder);
    }

    return response;
  };

  const onUploadFiles = async (
    path: string,
    files: File[],
    overwrite: boolean
  ) => {
    const response = await uploadFiles.execute(path, files, overwrite);
    await openFolder.refetch(folder);
    return response;
  };

  const onRemoveFile = async (path: string) => {
    const response = await removeFile.execute(path);
    await openFolder.refetch(folder);
    return response;
  };

  return (
    <Box
      my={24}
      p={4}
      minH="62vh"
      borderWidth={1}
      borderRadius={8}
      boxShadow="lg"
    >
      <Bar
        create={{ loading: createFolder.loading, execute: onCreateFolder }}
        upload={{ loading: uploadFiles.loading, execute: onUploadFiles }}
      />
      <Workspace
        openFolder={openFolder}
        removeFolder={{
          loading: removeFolder.loading,
          execute: onRemoveFolder,
        }}
        renameFolder={{
          loading: renameFolder.loading,
          execute: onRenameFolder,
        }}
        downloadFile={downloadFile}
        removeFile={{ loading: removeFile.loading, execute: onRemoveFile }}
        renameFile={{
          loading: renameFile.loading,
          execute: onRenameFile,
        }}
      />
    </Box>
  );
};
