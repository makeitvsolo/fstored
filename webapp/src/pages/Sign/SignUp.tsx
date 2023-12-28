import { useState } from "react";
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
  Alert,
  AlertIcon,
  AlertTitle,
  AlertDescription,
} from "@chakra-ui/react";

import { useSignUp, Message } from "@service";

export interface SignUpProps {
  onSwitch: () => void;
}

const SignUpForm = () => {
  const { loading, execute } = useSignUp();

  const [credentials, setCredentials] = useState({ name: "", password: "" });
  const [message, setMessage] = useState<Message>({} as Message);

  const onSubmit = async () => {
    setMessage(await execute(credentials.name, credentials.password));
  };

  return (
    <Box minW="container.sm" borderWidth={1} borderRadius={8} boxShadow="lg">
      <Stack p={12} spacing={4}>
        {message.error && (
          <Alert status="error">
            <AlertIcon />
            <AlertTitle>error:</AlertTitle>
            <AlertDescription>{message.error}</AlertDescription>
          </Alert>
        )}
        {message.ok && (
          <Alert status="success">
            <AlertIcon />
            <AlertTitle>success:</AlertTitle>
            <AlertDescription>{message.ok}</AlertDescription>
          </Alert>
        )}
        <FormControl isRequired>
          <FormLabel>Name</FormLabel>
          <Input
            aria-label="username"
            onChange={(e) =>
              setCredentials({ ...credentials, name: e.target.value })
            }
          />
        </FormControl>
        <FormControl isRequired>
          <FormLabel>Password</FormLabel>
          <Input
            aria-label="password"
            type="password"
            onChange={(e) =>
              setCredentials({ ...credentials, password: e.target.value })
            }
          />
        </FormControl>
        <Button
          colorScheme="blue"
          variant="outline"
          isLoading={loading.current}
          onClick={onSubmit}
        >
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
