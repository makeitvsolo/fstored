import { Divider, useColorMode } from "@chakra-ui/react";

import { Header } from "./Header";
import { Main } from "./Main";

export const Layout = () => {
  const { colorMode, toggleColorMode } = useColorMode();

  return (
    <>
      <Header theme={colorMode} toggleTheme={toggleColorMode} />
      <Divider />
      <Main />
    </>
  );
};
