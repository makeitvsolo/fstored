import { Box } from "@chakra-ui/react";

import { useCurrentFolder } from "@service";

import { Bar } from "./Bar";
import { Workspace } from "./Workspace";

export const Storage = () => {
  const currentFolder = useCurrentFolder();

  return (
    <Box
      my={24}
      p={4}
      minH="62vh"
      borderWidth={1}
      borderRadius={8}
      boxShadow="lg"
    >
      <Bar />
      <Workspace currentFolder={currentFolder} />
    </Box>
  );
};
