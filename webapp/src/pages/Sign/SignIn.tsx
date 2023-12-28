import { useState } from "react";
import { useNavigate } from "react-router-dom";
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

import { useSignIn, Message } from "@service";

export interface SignInProps {
  onSwitch: () => void;
}

const SignInForm = () => {
  const { loading, execute } = useSignIn();

  const navigate = useNavigate();
  const [credentials, setCredentials] = useState({ name: "", password: "" });
  const [message, setMessage] = useState<Message>({} as Message);

  const onSubmit = async () => {
    const msg = await execute(credentials.name, credentials.password);

    if (msg.ok) {
      navigate("/");
    }

    setMessage(msg);
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
