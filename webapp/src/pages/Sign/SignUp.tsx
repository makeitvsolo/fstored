import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Heading,
  Input,
  Stack,
  VStack,
  Text,
} from "@chakra-ui/react";

export interface SignUpProps {
  onSwitch: () => void;
}

const SignUpForm = () => {
  return (
    <Box minW="container.sm" borderWidth={1} borderRadius={8} boxShadow="lg">
      <Stack p={12} spacing={4}>
        <FormControl isRequired>
          <FormLabel>Name</FormLabel>
          <Input aria-label="username" />
        </FormControl>
        <FormControl isRequired>
          <FormLabel>Password</FormLabel>
          <Input aria-label="password" type="password" />
        </FormControl>
        <Button colorScheme="blue" variant="outline">
          Sign up
        </Button>
      </Stack>
    </Box>
  );
};

export const SignUp = ({ onSwitch }: SignUpProps) => {
  return (
    <VStack m={32}>
      <Heading>Sign up</Heading>
      <SignUpForm />
      <Text fontSize="lg">
        Already have an account?{" "}
        <Button colorScheme="blue" variant="link" onClick={onSwitch}>
          Sign in
        </Button>
      </Text>
    </VStack>
  );
};
