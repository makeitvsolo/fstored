import { Box } from "@chakra-ui/react";
import { Bar } from "./Bar";
import { Workspace } from "./Workspace";

export const Storage = () => {
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
      <Workspace />
    </Box>
  );
};
