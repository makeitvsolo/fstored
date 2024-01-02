import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Heading,
  VStack,
  Text,
  Button,
  Box,
  Stack,
  Alert,
  AlertIcon,
  AlertTitle,
  AlertDescription,
  FormControl,
  FormLabel,
  Input,
  FormHelperText,
} from "@chakra-ui/react";

import { Message, useSignIn } from "@service";

interface SignInFormProps {
  onSubmit: (name: string, password: string) => Promise<Message>;
  isLoading: boolean;
}

export interface SignInProps {
  onSwitch: () => void;
}

const SignInForm = ({ onSubmit, isLoading }: SignInFormProps) => {
  const [credentials, setCredentials] = useState({ name: "", password: "" });
  const [message, setMessage] = useState<Message>({} as Message);

  const onSignIn = async () => {
    const response = await onSubmit(credentials.name, credentials.password);
    setMessage(response);
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
          <FormHelperText>Minimum 5 symbols.</FormHelperText>
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
          <FormHelperText>Minimum 5 symbols.</FormHelperText>
        </FormControl>
        <Button
          colorScheme="blue"
          variant="outline"
          isLoading={isLoading}
          onClick={onSignIn}
        >
          Sign in
        </Button>
      </Stack>
    </Box>
  );
};

export const SignIn = ({ onSwitch }: SignInProps) => {
  const navigate = useNavigate();
  const { loading, execute } = useSignIn();

  const onSubmit = async (name: string, password: string) => {
    const response = await execute(name, password);

    if (response.ok) {
      navigate("/");
    }

    return response;
  };

  return (
    <VStack m={32}>
      <Heading>Sign in</Heading>
      <SignInForm onSubmit={onSubmit} isLoading={loading} />
      <Text fontSize="lg">
        Do not have an account?{" "}
        <Button colorScheme="blue" variant="link" onClick={onSwitch}>
          Sign up
        </Button>
      </Text>
    </VStack>
  );
};
