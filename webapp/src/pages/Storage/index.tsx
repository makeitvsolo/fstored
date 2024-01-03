import { Box } from "@chakra-ui/react";

import { useCreateFolder, useCurrentFolder } from "@service";

import { Bar } from "./Bar";
import { Workspace } from "./Workspace";

export const Storage = () => {
  const currentFolder = useCurrentFolder();
  const create = useCreateFolder();

  const onCreate = async (name: string) => {
    const response = await create.execute(name);
    await currentFolder.refetch();
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
      <Bar create={{ loading: create.loading, execute: onCreate }} />
      <Workspace currentFolder={currentFolder} />
    </Box>
  );
};
