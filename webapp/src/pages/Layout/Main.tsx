import { Outlet } from "react-router-dom";
import { Box, Container } from "@chakra-ui/react";

export const Main = () => {
  return (
    <Box>
      <Container maxW="container.xl">
        <Outlet />
      </Container>
    </Box>
  );
};
