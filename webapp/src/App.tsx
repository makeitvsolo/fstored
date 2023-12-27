import { Route, Routes } from "react-router-dom";

import { Layout } from "@pages";

export const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Layout />} />
    </Routes>
  );
};
