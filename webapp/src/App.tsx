import { Route, Routes } from "react-router-dom";

import { Empty, Home, Layout } from "@pages";

export const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Home />} />
        <Route path="*" element={<Empty />} />
      </Route>
    </Routes>
  );
};
