import { useNavigate } from "react-router-dom";
import { Divider, useColorMode } from "@chakra-ui/react";

import { useSignOut } from "@service";

import { Header } from "./Header";
import { Main } from "./Main";

export const Layout = () => {
  const navigate = useNavigate();
  const { colorMode, toggleColorMode } = useColorMode();
  const { execute } = useSignOut();

  const onSignOut = async () => {
    const msg = await execute();
    if (msg.ok) {
      navigate("/");
    }
  };

  return (
    <>
      <Header
        theme={colorMode}
        toggleTheme={toggleColorMode}
        onSignOut={onSignOut}
      />
      <Divider />
      <Main />
    </>
  );
};
