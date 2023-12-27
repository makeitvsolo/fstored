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

export interface SignInProps {
  onSwitch: () => void;
}

const SignInForm = () => {
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
          Sign in
        </Button>
      </Stack>
    </Box>
  );
};

export const SignIn = ({ onSwitch }: SignInProps) => {
  return (
    <VStack m={32}>
      <Heading>Sign in</Heading>
      <SignInForm />
      <Text fontSize="lg">
        Do not have an account?{" "}
        <Button colorScheme="blue" variant="link" onClick={onSwitch}>
          Sign up
        </Button>
      </Text>
    </VStack>
  );
};
