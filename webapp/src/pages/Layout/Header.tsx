import { Link as ReactRouterLink } from "react-router-dom";
import { AtSignIcon, ChevronDownIcon, NotAllowedIcon } from "@chakra-ui/icons";
import {
  Box,
  Button,
  ButtonGroup,
  Container,
  Flex,
  Heading,
  Link,
  Menu,
  MenuButton,
  MenuItem,
  MenuList,
} from "@chakra-ui/react";

import { useUserStore } from "@store";

const Bar = () => {
  const user = useUserStore((state) => state.activeUser);

  return (
    <ButtonGroup spacing={8}>
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
            <MenuItem icon={<NotAllowedIcon />}>Sign out</MenuItem>
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
  return (
    <Box py={3} bg="blue.100">
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
