import { Divider } from "@chakra-ui/react";

import { Header } from "./Header";
import { Main } from "./Main";

export const Layout = () => {
  return (
    <>
      <Header />
      <Divider />
      <Main />
    </>
  );
};
