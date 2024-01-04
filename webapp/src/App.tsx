import { Route, Routes } from "react-router-dom";

import { Empty, Home, Layout, Sign, Storage } from "@pages";
import { useUserStore } from "@store";

export const App = () => {
  const user = useUserStore((state) => state.activeUser);

  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        {user !== null ? (
          <Route index element={<Storage />} />
        ) : (
          <Route index element={<Home />} />
        )}
        <Route path="sign" element={<Sign />} />
        <Route path="*" element={<Empty />} />
      </Route>
    </Routes>
  );
};
