import { Link as ReactRouterLink, useNavigate } from "react-router-dom";
import {
  AtSignIcon,
  ChevronDownIcon,
  MoonIcon,
  NotAllowedIcon,
  SunIcon,
} from "@chakra-ui/icons";
import {
  Box,
  Button,
  ButtonGroup,
  Container,
  Flex,
  Heading,
  IconButton,
  Link,
  Menu,
  MenuButton,
  MenuItem,
  MenuList,
  useColorMode,
} from "@chakra-ui/react";

import { useUserStore } from "@store";
import { useSignOut } from "@service";

const Bar = () => {
  const navigate = useNavigate();
  const { colorMode, toggleColorMode } = useColorMode();

  const user = useUserStore((state) => state.activeUser);
  const { execute } = useSignOut();

  const onOut = async () => {
    const msg = await execute();
    if (msg.ok) {
      navigate("/");
    }
  };

  return (
    <ButtonGroup spacing={8}>
      <IconButton
        aria-label="switch-theme"
        colorScheme="blue"
        variant="ghost"
        icon={colorMode === "light" ? <MoonIcon /> : <SunIcon />}
        onClick={toggleColorMode}
      />
      {user !== null ? (
        <Menu>
          <MenuButton
            as={Button}
            leftIcon={<AtSignIcon />}
            rightIcon={<ChevronDownIcon />}
            colorScheme="blue"
          >
            {user.name}
          </MenuButton>
          <MenuList>
            <MenuItem icon={<NotAllowedIcon />} onClick={onOut}>
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
