import { Link as ReactRouterLink } from "react-router-dom";
import { MoonIcon, SunIcon } from "@chakra-ui/icons";
import {
  Box,
  Button,
  ButtonGroup,
  ColorMode,
  Container,
  Flex,
  Heading,
  IconButton,
  Link,
} from "@chakra-ui/react";

export interface BarProps {
  theme: ColorMode;
  toggleTheme: () => void;
}

export interface HeaderProps extends BarProps {}

const Bar = ({ theme, toggleTheme }: BarProps) => {
  return (
    <ButtonGroup spacing={8}>
      <IconButton
        aria-label="switch-theme"
        colorScheme="blue"
        variant="ghost"
        icon={theme === "light" ? <MoonIcon /> : <SunIcon />}
        onClick={toggleTheme}
      />
      <Button as={ReactRouterLink} colorScheme="blue" to="/sign">
        Sign in
      </Button>
    </ButtonGroup>
  );
};

export const Header = ({ theme, toggleTheme }: HeaderProps) => {
  return (
    <Box py={3} bg={theme === "light" ? "blue.200" : "gray.700"}>
      <Container maxW="container.xl">
        <Flex justifyContent="space-between" alignItems="center">
          <Heading>
            <Link as={ReactRouterLink} to="/">
              FStored
            </Link>
          </Heading>
          <Bar theme={theme} toggleTheme={toggleTheme} />
        </Flex>
      </Container>
    </Box>
  );
};
