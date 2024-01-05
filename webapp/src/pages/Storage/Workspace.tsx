import { useEffect, useState } from "react";
import {
  ArrowUpDownIcon,
  DeleteIcon,
  DownloadIcon,
  EditIcon,
} from "@chakra-ui/icons";
import {
  Box,
  Spinner,
  StackDivider,
  VStack,
  Text,
  Button,
  Flex,
  Icon,
  HStack,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  MenuDivider,
  useDisclosure,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalBody,
  FormControl,
  FormHelperText,
  Input,
  Alert,
  AlertIcon,
  AlertTitle,
  AlertDescription,
  ModalFooter,
  ButtonGroup,
} from "@chakra-ui/react";

import { FolderContent } from "@api";
import { useFoldersStore } from "@store";
import { Message } from "@service";

interface FileProps {
  path: string;
  size: number;
  modificationTime: Date;

  download: (path: string) => Promise<void>;
  remove: (path: string) => Promise<Message>;
  rename: {
    loading: boolean;
    execute: (path: string, name: string) => Promise<Message>;
  };
}

interface FolderProps {
  path: string;

  open: (path: string) => Promise<void>;
  remove: (path: string) => Promise<Message>;
  rename: {
    loading: boolean;
    execute: (path: string, name: string) => Promise<Message>;
  };
}

export interface WorkspaceProps {
  openFolder: {
    loading: boolean;
    data: FolderContent | null;
    refetch: (path: string) => Promise<void>;
  };
  renameFolder: {
    loading: boolean;
    execute: (path: string, name: string) => Promise<Message>;
  };
  removeFolder: {
    loading: boolean;
    execute: (path: string) => Promise<Message>;
  };

  downloadFile: {
    fetch: (path: string) => Promise<void>;
  };
  renameFile: {
    loading: boolean;
    execute: (path: string, name: string) => Promise<Message>;
  };
  removeFile: {
    loading: boolean;
    execute: (path: string) => Promise<Message>;
  };
}

const File = ({
  path,
  size,
  modificationTime,
  download,
  remove,
  rename,
}: FileProps) => {
  const disclosure = useDisclosure();
  const [name, setName] = useState("");
  const [message, setMessage] = useState({} as Message);

  const toName = (path: string) => {
    return path.split("/").reduce((_, second) => second);
  };

  const toFileSize = (bytes: number) => {
    const k = 1024;
    const names = [
      "Bytes",
      "KiB",
      "MiB",
      "GiB",
      "TiB",
      "PiB",
      "EiB",
      "ZiB",
      "YiB",
    ];

    if (!+bytes) {
      return "0 Bytes";
    }

    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return `${Math.floor(bytes / Math.pow(k, i))} ${names[i]}`;
  };

  const onClose = () => {
    setMessage({} as Message);
    disclosure.onClose();
  };

  const onRename = async () => {
    const response = await rename.execute(path, name);

    if (response.ok) {
      onClose();
    }

    setMessage(response);
  };

  return (
    <>
      <Menu isLazy>
        <MenuButton
          as={Button}
          size="sm"
          width="full"
          colorScheme="blue"
          variant="ghost"
        >
          <Flex width="full" justifyContent="space-between" alignItems="center">
            <Text>
              <Icon fill="currentColor">
                <path d="M16,2l4,4H16ZM14,2H5A1,1,0,0,0,4,3V21a1,1,0,0,0,1,1H19a1,1,0,0,0,1-1V8H14Z" />
              </Icon>{" "}
              {toName(path)}
            </Text>
            <HStack spacing={14}>
              <Text>{modificationTime.toString()}</Text>
              <Text>{toFileSize(size)}</Text>
            </HStack>
          </Flex>
        </MenuButton>

        <MenuList>
          <MenuItem
            onClick={async () => await download(path)}
            icon={<DownloadIcon />}
          >
            Download
          </MenuItem>
          <MenuDivider />
          <MenuItem onClick={disclosure.onOpen} icon={<EditIcon />}>Rename</MenuItem>
          <MenuItem
            onClick={async () => await remove(path)}
            icon={<DeleteIcon />}
          >
            Remove
          </MenuItem>
        </MenuList>
      </Menu>

      <Modal isOpen={disclosure.isOpen} onClose={onClose} isCentered>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Rename</ModalHeader>
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
                  placeholder="file name"
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
              <Button onClick={onRename} isLoading={rename.loading}>
                Rename
              </Button>
              <Button onClick={onClose} isLoading={rename.loading}>
                Cancel
              </Button>
            </ButtonGroup>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  );
};

const Folder = ({ path, open, remove, rename }: FolderProps) => {
  const disclosure = useDisclosure();
  const [name, setName] = useState("");
  const [message, setMessage] = useState({} as Message);

  const toName = (path: string) => {
    return path
      .split("/")
      .reduce((first, second) => (second !== "" ? second : first));
  };

  const onClose = () => {
    setMessage({} as Message);
    disclosure.onClose();
  };

  const onRename = async () => {
    const response = await rename.execute(path, name);

    if (response.ok) {
      onClose();
    }

    setMessage(response);
  };

  return (
    <>
      <Menu isLazy>
        <MenuButton
          as={Button}
          size="sm"
          width="full"
          colorScheme="blue"
          variant="ghost"
        >
          <Flex width="full" justifyContent="space-between" alignItems="center">
            <Text>
              <Icon fill="currentColor">
                <path d="M2 7c0-1.4 0-2.1.272-2.635a2.5 2.5 0 0 1 1.093-1.093C3.9 3 4.6 3 6 3h1.431c.94 0 1.409 0 1.835.13a3 3 0 0 1 1.033.552c.345.283.605.674 1.126 1.455L12 6h6c1.4 0 2.1 0 2.635.272a2.5 2.5 0 0 1 1.092 1.093C22 7.9 22 8.6 22 10v5c0 1.4 0 2.1-.273 2.635a2.5 2.5 0 0 1-1.092 1.092C20.1 19 19.4 19 18 19H6c-1.4 0-2.1 0-2.635-.273a2.5 2.5 0 0 1-1.093-1.092C2 17.1 2 16.4 2 15V7z" />
              </Icon>{" "}
              {toName(path)}
            </Text>
          </Flex>
        </MenuButton>

        <MenuList>
          <MenuItem
            icon={<ArrowUpDownIcon />}
            onClick={async () => await open(path)}
          >
            Open
          </MenuItem>
          <MenuDivider />
          <MenuItem onClick={disclosure.onOpen} icon={<EditIcon />}>
            Rename
          </MenuItem>
          <MenuItem
            onClick={async () => await remove(path)}
            icon={<DeleteIcon />}
          >
            Remove
          </MenuItem>
        </MenuList>
      </Menu>

      <Modal isOpen={disclosure.isOpen} onClose={onClose} isCentered>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Rename</ModalHeader>
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
              <Button onClick={onRename} isLoading={rename.loading}>
                Rename
              </Button>
              <Button onClick={onClose} isLoading={rename.loading}>
                Cancel
              </Button>
            </ButtonGroup>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  );
};

export const Workspace = ({
  openFolder,
  removeFolder,
  renameFolder,
  downloadFile,
  removeFile,
  renameFile
}: WorkspaceProps) => {
  const folder = useFoldersStore((state) => state.folder);

  useEffect(() => {
    openFolder.refetch(folder);
  }, []);

  return (
    <Box my={6} h="60vh" p={2} borderWidth={1} overflowY="auto">
      <VStack divider={<StackDivider />}>
        {openFolder.loading ? (
          <Box mt={24}>
            <Spinner />
          </Box>
        ) : openFolder.data?.children.length === 0 ? (
          <Box mt={24}>
            <Text as="h3">There's nothing in here.</Text>
          </Box>
        ) : (
          openFolder.data?.children.map((child) =>
            child.resource === "files" ? (
              <File
                key={child.path}
                path={child.path}
                size={child.size!}
                modificationTime={child.modificationTime!}
                download={downloadFile.fetch}
                remove={removeFile.execute}
                rename={renameFile}
              />
            ) : (
              <Folder
                key={child.path}
                path={child.path}
                open={openFolder.refetch}
                remove={removeFolder.execute}
                rename={renameFolder}
              />
            )
          )
        )}
      </VStack>
    </Box>
  );
};
