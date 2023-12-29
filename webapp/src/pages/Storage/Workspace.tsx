import {
  Box,
  Flex,
  Icon,
  StackDivider,
  VStack,
  Text,
  HStack,
  Button,
} from "@chakra-ui/react";

interface FileProps {
  name: string;
}

interface FolderProps {
  name: string;
}

export interface ResourceProps {
  type: "file" | "folder";
  name: string;
}

const File = ({ name }: FileProps) => {
  return (
    <Button size="sm" width="full" colorScheme="blue" variant="ghost">
      <Flex width="full" justifyContent="space-between" alignItems="center">
        <Text>
          <Icon fill="currentColor">
            <path d="M16,2l4,4H16ZM14,2H5A1,1,0,0,0,4,3V21a1,1,0,0,0,1,1H19a1,1,0,0,0,1-1V8H14Z" />
          </Icon>{" "}
          {name}
        </Text>
        <HStack spacing={14}>
          <Text>at 17 Oct. 21:19</Text>
          <Text>64kb</Text>
        </HStack>
      </Flex>
    </Button>
  );
};

const Folder = ({ name }: FolderProps) => {
  return (
    <Button size="sm" width="full" colorScheme="blue" variant="ghost">
      <Flex width="full" justifyContent="space-between" alignItems="center">
        <Text>
          <Icon fill="currentColor">
            <path d="M2 7c0-1.4 0-2.1.272-2.635a2.5 2.5 0 0 1 1.093-1.093C3.9 3 4.6 3 6 3h1.431c.94 0 1.409 0 1.835.13a3 3 0 0 1 1.033.552c.345.283.605.674 1.126 1.455L12 6h6c1.4 0 2.1 0 2.635.272a2.5 2.5 0 0 1 1.092 1.093C22 7.9 22 8.6 22 10v5c0 1.4 0 2.1-.273 2.635a2.5 2.5 0 0 1-1.092 1.092C20.1 19 19.4 19 18 19H6c-1.4 0-2.1 0-2.635-.273a2.5 2.5 0 0 1-1.093-1.092C2 17.1 2 16.4 2 15V7z" />
          </Icon>{" "}
          {name}
        </Text>
      </Flex>
    </Button>
  );
};

const Resource = ({ type, name }: ResourceProps) => {
  return type === "file" ? <File name={name} /> : <Folder name={name} />;
};

export const Workspace = () => {
  return (
    <Box my={6} h="60vh" p={2} borderWidth={1} overflowY="auto">
      <VStack divider={<StackDivider />}>
        <Resource type="folder" name="folder" />
        <Resource type="file" name="file.ext" />
      </VStack>
    </Box>
  );
};
