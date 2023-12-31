import { useState } from "react";
import Dropzone from "react-dropzone";
import {
  ArrowUpDownIcon,
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
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  Button,
  ButtonGroup,
  Flex,
  FormControl,
  FormHelperText,
  IconButton,
  Input,
  InputGroup,
  InputLeftElement,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Popover,
  PopoverArrow,
  PopoverBody,
  PopoverCloseButton,
  PopoverContent,
  PopoverHeader,
  PopoverTrigger,
  Spinner,
  StackDivider,
  Text,
  VStack,
  useDisclosure,
} from "@chakra-ui/react";

import { MatchingResources } from "@api";
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

interface PathProps {
  open: (path: string) => Promise<void>;
}

interface SearchProps {
  search: {
    loading: boolean;
    data: MatchingResources | null;
    refetch: (name: string) => Promise<void>;
  };

  open: (path: string) => Promise<void>;
  download: (path: string) => Promise<void>;
}

interface RightMenuProps extends NewFolderProps, UploadProps {}

interface LeftMenuProps extends PathProps, SearchProps {}

export interface BarProps extends LeftMenuProps, RightMenuProps {}

const Path = ({ open }: PathProps) => {
  const folder = useFoldersStore((state) => state.folder);
  const parts = `Home${folder}`.split("/").filter((part) => part);

  const toName = (parts: string[]) => {
    return parts[parts.length - 1];
  };

  const onOpen = async (partIdx: number) => {
    const path =
      partIdx === 0
        ? "/"
        : `/${parts
            .slice(0, partIdx + 1)
            .filter((part) => part !== "Home")
            .join("/")}/`;

    console.log(path);
    await open(path);
  };

  return (
    <Popover>
      <PopoverTrigger>
        <Button leftIcon={<ViewIcon />}>
          <Text fontSize="xs">{toName(parts)}</Text>
        </Button>
      </PopoverTrigger>
      <PopoverContent>
        <PopoverArrow />
        <PopoverCloseButton />
        <PopoverHeader>Current path</PopoverHeader>
        <PopoverBody>
          <Breadcrumb>
            {parts.map((part, idx) => (
              <BreadcrumbItem key={part}>
                <BreadcrumbLink onClick={async () => await onOpen(idx)}>
                  {part}
                </BreadcrumbLink>
              </BreadcrumbItem>
            ))}
          </Breadcrumb>
        </PopoverBody>
      </PopoverContent>
    </Popover>
  );
};

const Search = ({ search, open, download }: SearchProps) => {
  const disclosure = useDisclosure();

  const onOpen = async (path: string) => {
    await open(path);
    disclosure.onClose();
  };

  const onDownload = async (path: string) => {
    await download(path);
    disclosure.onClose();
  };

  return (
    <>
      <Button leftIcon={<SearchIcon />} onClick={disclosure.onOpen}>
        <Text fontSize="xs">Search</Text>
      </Button>

      <Modal isOpen={disclosure.isOpen} onClose={disclosure.onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>
            <VStack my={2}>
              <Text>Search</Text>
              <InputGroup>
                <InputLeftElement pointerEvents="none">
                  <SearchIcon />
                </InputLeftElement>
                <Input
                  placeholder="resource name"
                  onChange={async (e) => await search.refetch(e.target.value)}
                />
              </InputGroup>
            </VStack>
          </ModalHeader>
          <ModalBody>
            <VStack divider={<StackDivider />}>
              {search.loading ? (
                <Box>
                  <Spinner />
                </Box>
              ) : search.data?.objects.length === 0 ? (
                <Box>
                  <Text as="h3">Nothing was found.</Text>
                </Box>
              ) : (
                search.data?.objects.map((obj) => (
                  <Flex
                    key={obj.path}
                    width="full"
                    justifyContent="space-between"
                    alignItems="center"
                  >
                    <Text>{obj.path}</Text>
                    {obj.resource === "files" ? (
                      <IconButton
                        aria-label="download file"
                        icon={<DownloadIcon />}
                        onClick={async () => await onDownload(obj.path)}
                      />
                    ) : (
                      <IconButton
                        aria-label="open folder"
                        icon={<ArrowUpDownIcon />}
                        onClick={async () => await onOpen(obj.path)}
                      />
                    )}
                  </Flex>
                ))
              )}
            </VStack>
          </ModalBody>
        </ModalContent>
      </Modal>
    </>
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

const LeftMenu = ({ open, search, download }: LeftMenuProps) => {
  return (
    <ButtonGroup colorScheme="blue" variant="outline" size="sm" spacing={4}>
      <Path open={open} />
      <Search search={search} open={open} download={download} />
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

export const Bar = ({ create, upload, open, search, download }: BarProps) => {
  return (
    <Flex mx={4} justifyContent="space-between" alignItems="center">
      <LeftMenu open={open} search={search} download={download} />
      <RightMenu create={create} upload={upload} />
    </Flex>
  );
};
