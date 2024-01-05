import { Link as ReactRouterLink } from "react-router-dom";
import { AtSignIcon, MoonIcon, NotAllowedIcon, SunIcon } from "@chakra-ui/icons";
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
  Menu,
  MenuButton,
  MenuItem,
  MenuList,
} from "@chakra-ui/react";
import { useUserStore } from "@store";

export interface BarProps {
  theme: ColorMode;
  toggleTheme: () => void;
  
  onSignOut: () => Promise<void>;
}

export interface HeaderProps extends BarProps {}

const Bar = ({ theme, toggleTheme, onSignOut }: BarProps) => {
  const user = useUserStore((state) => state.activeUser);
  
  return (
    <ButtonGroup spacing={8}>
      <IconButton
        aria-label="switch-theme"
        colorScheme="blue"
        variant="ghost"
        icon={theme === "light" ? <MoonIcon /> : <SunIcon />}
        onClick={toggleTheme}
      />
      {user !== null ? (
        <Menu>
          <MenuButton
            as={Button}
            leftIcon={<AtSignIcon />}
            colorScheme="blue"
          >
            {user.name}
          </MenuButton>
          <MenuList>
            <MenuItem icon={<NotAllowedIcon />} onClick={onSignOut}>
              Sign out
            </MenuItem>
          </MenuList>
        </Menu>
      ) : (
        <Button as={ReactRouterLink} colorScheme="blue" to="/sign">
          Sign in
        </Button>
      )}
    </ButtonGroup>
  );
};

export const Header = ({ theme, toggleTheme, onSignOut }: HeaderProps) => {
  return (
    <Box py={3} bg={theme === "light" ? "blue.200" : "gray.700"}>
      <Container maxW="container.xl">
        <Flex justifyContent="space-between" alignItems="center">
          <Heading>
            <Link as={ReactRouterLink} to="/">
              FStored
            </Link>
          </Heading>
          <Bar theme={theme} toggleTheme={toggleTheme} onSignOut={onSignOut} />
        </Flex>
      </Container>
    </Box>
  );
};
