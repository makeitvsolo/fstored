import { useState } from "react";
import Dropzone from "react-dropzone";
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
  VStack,
  useDisclosure,
} from "@chakra-ui/react";

import { Message } from "@service";
import { useFoldersStore } from "@store";

interface NewFolderProps {
  create: {
    loading: boolean;
    execute: (path: string, name: string) => Promise<Message>;
  };
}

interface UploadProps {
  upload: {
    loading: boolean;
    execute: (
      path: string,
      files: File[],
      overwrite: boolean
    ) => Promise<Message>;
  };
}

interface RightMenuProps extends NewFolderProps, UploadProps {}

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
  const folder = useFoldersStore((state) => state.folder);

  const disclosure = useDisclosure();
  const [name, setName] = useState("");
  const [message, setMessage] = useState({} as Message);

  const onClose = () => {
    setMessage({} as Message);
    disclosure.onClose();
  };

  const onCreate = async () => {
    const response = await create.execute(folder, name);

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

const Upload = ({ upload }: UploadProps) => {
  const folder = useFoldersStore((state) => state.folder);

  const disclosure = useDisclosure();
  const [files, setFiles] = useState<File[]>([]);
  const [message, setMessage] = useState({} as Message);

  const onClose = () => {
    setMessage({} as Message);
    setFiles([]);
    disclosure.onClose();
  };

  const onUpload = async () => {
    const overwrite = false;
    const response = await upload.execute(folder, files, overwrite);

    if (response.ok) {
      onClose();
    }

    setMessage(response);
  };

  const onOverwrite = async () => {
    const overwrite = true;
    const response = await upload.execute(folder, files, overwrite);

    if (response.ok) {
      onClose();
    }

    setMessage(response);
  };

  return (
    <>
      <Button leftIcon={<DownloadIcon />} onClick={disclosure.onOpen}>
        <Text fontSize="xs">Upload</Text>
      </Button>

      <Modal isOpen={disclosure.isOpen} onClose={onClose} isCentered>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Upload files</ModalHeader>
          <ModalBody>
            <Box my={2}>
              {message.error && (
                <Alert status="error">
                  <AlertIcon />
                  <AlertTitle>error:</AlertTitle>
                  <AlertDescription>{message.error}</AlertDescription>
                </Alert>
              )}
              <Dropzone onDrop={(acceptedFiles) => setFiles(acceptedFiles)}>
                {({ getRootProps, getInputProps }) => (
                  <Box
                    {...getRootProps()}
                    p={4}
                    borderWidth={1}
                    borderRadius={4}
                    boxShadow="lg"
                    cursor="pointer"
                  >
                    <input {...getInputProps()} />
                    <VStack spacing={1}>
                      <Text>
                        Drag 'n' drop some files here, or click to select files
                      </Text>
                      {files.length !== 0 && (
                        <Text as="i" noOfLines={1}>
                          selected: {files.map((file) => file.name)}
                        </Text>
                      )}
                    </VStack>
                  </Box>
                )}
              </Dropzone>
            </Box>
          </ModalBody>
          <ModalFooter>
            <ButtonGroup colorScheme="blue" variant="ghost">
              <Button onClick={onUpload} isLoading={upload.loading}>
                Upload
              </Button>
              <Button onClick={onOverwrite} isLoading={upload.loading}>
                Overwrite
              </Button>
              <Button onClick={onClose} isLoading={upload.loading}>
                Cancel
              </Button>
            </ButtonGroup>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
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

const RightMenu = ({ create, upload }: RightMenuProps) => {
  return (
    <ButtonGroup colorScheme="blue" variant="outline" size="sm" spacing={4}>
      <NewFolder create={create} />
      <Upload upload={upload} />
    </ButtonGroup>
  );
};

export const Bar = ({ create, upload }: BarProps) => {
  return (
    <Flex mx={4} justifyContent="space-between" alignItems="center">
      <LeftMenu />
      <RightMenu create={create} upload={upload} />
    </Flex>
  );
};
