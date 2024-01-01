import { Link as ReactRouterLink } from "react-router-dom";
import { MoonIcon, SunIcon } from "@chakra-ui/icons";
import {
  Box,
  Button,
  ButtonGroup,
  Container,
  Flex,
  Heading,
  IconButton,
  Link,
  useColorMode,
} from "@chakra-ui/react";

const Bar = () => {
  const { colorMode, toggleColorMode } = useColorMode();

  return (
    <ButtonGroup spacing={8}>
      <IconButton
        aria-label="switch-theme"
        colorScheme="blue"
        variant="ghost"
        icon={colorMode === "light" ? <MoonIcon /> : <SunIcon />}
        onClick={toggleColorMode}
      />
      <Button as={ReactRouterLink} colorScheme="blue" to="/sign">
        Sign in
      </Button>
    </ButtonGroup>
  );
};

export const Header = () => {
  const { colorMode } = useColorMode();

  return (
    <Box py={3} bg={colorMode === "light" ? "blue.200" : "gray.700"}>
      <Container maxW="container.xl">
        <Flex justifyContent="space-between" alignItems="center">
          <Heading>
            <Link as={ReactRouterLink} to="/">
              FStored
            </Link>
          </Heading>
          <Bar />
        </Flex>
      </Container>
    </Box>
  );
};
