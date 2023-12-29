import { Route, Routes } from "react-router-dom";

import { Empty, Home, Layout, Sign, Storage } from "@pages";

export const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Home />} />
        <Route path="sign" element={<Sign />} />
        <Route path="storage" element={<Storage />} />
        <Route path="*" element={<Empty />} />
      </Route>
    </Routes>
  );
};
