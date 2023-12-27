import { Heading, Highlight, VStack, Text } from "@chakra-ui/react";

export const Empty = () => {
  return (
    <VStack m={32}>
      <Heading as="h1" size="4xl" lineHeight="tall">
        <Highlight
          query="Empty"
          styles={{ px: "6", py: "4", rounded: "full", bg: "blue.100" }}
        >
          Empty page
        </Highlight>
      </Heading>
      <Text fontSize="3xl">There's nothing here.</Text>
    </VStack>
  );
};
