import { useState } from "react";
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

import { Message, useSignUp } from "@service";

interface SignUpFormProps {
  onSubmit: (name: string, password: string) => Promise<Message>;
  isLoading: boolean;
}

export interface SignUpProps {
  onSwitch: () => void;
}

const SignUpForm = ({ onSubmit, isLoading }: SignUpFormProps) => {
  const [credentials, setCredentials] = useState({ name: "", password: "" });
  const [message, setMessage] = useState<Message>({} as Message);

  const onSignUp = async () => {
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
          onClick={onSignUp}
        >
          Sign up
        </Button>
      </Stack>
    </Box>
  );
};

export const SignUp = ({ onSwitch }: SignUpProps) => {
  const { loading, execute } = useSignUp();

  return (
    <VStack m={32}>
      <Heading>Sign up</Heading>
      <SignUpForm onSubmit={execute} isLoading={loading} />
      <Text fontSize="lg">
        Already have an account?{" "}
        <Button colorScheme="blue" variant="link" onClick={onSwitch}>
          Sign in
        </Button>
      </Text>
    </VStack>
  );
};
