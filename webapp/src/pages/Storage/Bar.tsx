import { useState } from "react";
import {
  DownloadIcon,
  PlusSquareIcon,
  SearchIcon,
  ViewIcon,
} from "@chakra-ui/icons";
import {
  Alert,
  AlertDescription,
  AlertIcon,
  AlertTitle,
  Box,
  Button,
  ButtonGroup,
  Flex,
  FormControl,
  FormHelperText,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Text,
  useDisclosure,
} from "@chakra-ui/react";

import { Message } from "@service";

interface NewFolderProps {
  create: {
    loading: boolean;
    execute: (name: string) => Promise<Message>;
  };
}

interface RightMenuProps extends NewFolderProps {}

interface LeftMenuProps {}

export interface BarProps extends LeftMenuProps, RightMenuProps {}

const Path = () => {
  return (
    <Button leftIcon={<ViewIcon />}>
      <Text fontSize="xs">Path</Text>
    </Button>
  );
};

const Search = () => {
  return (
    <Button leftIcon={<SearchIcon />}>
      <Text fontSize="xs">Search</Text>
    </Button>
  );
};

const NewFolder = ({ create }: NewFolderProps) => {
  const disclosure = useDisclosure();
  const [name, setName] = useState("");
  const [message, setMessage] = useState({} as Message);

  const onClose = () => {
    setMessage({} as Message);
    disclosure.onClose();
  };

  const onCreate = async () => {
    const response = await create.execute(name);

    if (response.ok) {
      onClose();
    }

    setMessage(response);
  };

  return (
    <>
      <Button leftIcon={<PlusSquareIcon />} onClick={disclosure.onOpen}>
        <Text fontSize="xs">New</Text>
      </Button>

      <Modal isOpen={disclosure.isOpen} onClose={onClose} isCentered>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Create Folder</ModalHeader>
          <ModalBody>
            <Box my={2}>
              {message.error && (
                <Alert status="error">
                  <AlertIcon />
                  <AlertTitle>error:</AlertTitle>
                  <AlertDescription>{message.error}</AlertDescription>
                </Alert>
              )}
              <FormControl>
                <Input
                  placeholder="folder name"
                  onChange={(e) => setName(e.target.value)}
                />
                <FormHelperText>
                  Only letters, numbers or symbols '.-_'
                </FormHelperText>
              </FormControl>
            </Box>
          </ModalBody>
          <ModalFooter>
            <ButtonGroup colorScheme="blue" variant="ghost">
              <Button onClick={onCreate} isLoading={create.loading}>
                Create
              </Button>
              <Button onClick={onClose} isLoading={create.loading}>
                Cancel
              </Button>
            </ButtonGroup>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  );
};

const Upload = () => {
  return (
    <Button leftIcon={<DownloadIcon />}>
      <Text fontSize="xs">Upload</Text>
    </Button>
  );
};

const LeftMenu = () => {
  return (
    <ButtonGroup colorScheme="blue" variant="outline" size="sm" spacing={4}>
      <Path />
      <Search />
    </ButtonGroup>
  );
};

const RightMenu = ({ create }: RightMenuProps) => {
  return (
    <ButtonGroup colorScheme="blue" variant="outline" size="sm" spacing={4}>
      <NewFolder create={create} />
      <Upload />
    </ButtonGroup>
  );
};

export const Bar = ({ create }: BarProps) => {
  return (
    <Flex mx={4} justifyContent="space-between" alignItems="center">
      <LeftMenu />
      <RightMenu create={create} />
    </Flex>
  );
};
