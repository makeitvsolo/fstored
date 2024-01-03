import { Box } from "@chakra-ui/react";

import { useCreateFolder, useOpenFolder, useUploadFiles } from "@service";

import { Bar } from "./Bar";
import { Workspace } from "./Workspace";

export const Storage = () => {
  const openFolder = useOpenFolder();
  const createFolder = useCreateFolder();
  const uploadFiles = useUploadFiles();

  const onCreateFolder = async (path: string, name: string) => {
    const response = await createFolder.execute(path, name);
    await openFolder.refetch(path);
    return response;
  };

  const onUploadFiles = async (
    path: string,
    files: File[],
    overwrite: boolean
  ) => {
    const response = await uploadFiles.execute(path, files, overwrite);
    await openFolder.refetch(path);
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
      <Workspace openFolder={openFolder} />
    </Box>
  );
};
