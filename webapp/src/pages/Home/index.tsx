import { ExternalLinkIcon } from "@chakra-ui/icons";
import { Heading, Highlight, VStack, Text, Link } from "@chakra-ui/react";

export const Home = () => {
  return (
    <VStack m={32}>
      <Heading as="h1" size="4xl" lineHeight="tall">
        <Highlight
          query="Fstored"
          styles={{ px: "6", py: "4", rounded: "full", bg: "blue.200" }}
        >
          FStored
        </Highlight>
      </Heading>
      <Text fontSize="3xl">Open source Cloud File Storage</Text>
      <Link href="https://github.com/makeitvsolo/fstored" isExternal>
        Source code <ExternalLinkIcon mx="2px" />
      </Link>
    </VStack>
  );
};
