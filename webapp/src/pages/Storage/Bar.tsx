import { DownloadIcon, PlusSquareIcon, SearchIcon } from "@chakra-ui/icons";
import { Button, ButtonGroup, Flex, Text, HStack } from "@chakra-ui/react";

const Path = () => {
  return (
    <>
      <Button colorScheme="blue" variant="ghost" size="sm">
        Path
      </Button>
    </>
  );
};

const Search = () => {
  return (
    <>
      <Button colorScheme="blue" variant="outline" size="sm">
        <HStack spacing={4}>
          <SearchIcon />
          <Text fontSize="xs">search resource</Text>
        </HStack>
      </Button>
    </>
  );
};

const Menu = () => {
  return (
    <ButtonGroup colorScheme="blue" variant="outline" size="sm" spacing={6}>
      <Button leftIcon={<PlusSquareIcon />}>New</Button>
      <Button leftIcon={<DownloadIcon />}>Upload</Button>
    </ButtonGroup>
  );
};

export const Bar = () => {
  return (
    <Flex mx={4} justifyContent="space-between" alignItems="center">
      <Search />
      <Path />
      <Menu />
    </Flex>
  );
};
