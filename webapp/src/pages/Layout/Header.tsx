import {
  Box,
  Button,
  ButtonGroup,
  Container,
  Flex,
  Heading,
} from "@chakra-ui/react";

const Bar = () => {
  return (
    <ButtonGroup spacing={8}>
      <Button colorScheme="blue">Sign in</Button>
    </ButtonGroup>
  );
};

export const Header = () => {
  return (
    <Box py={3} bg="blue.100">
      <Container maxW="container.xl">
        <Flex justifyContent="space-between" alignItems="center">
          <Heading>FStored</Heading>
          <Bar />
        </Flex>
      </Container>
    </Box>
  );
};
