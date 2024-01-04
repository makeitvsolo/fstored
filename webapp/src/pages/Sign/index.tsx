import { useState } from "react";

import { SignIn } from "./SignIn";
import { SignUp } from "./SignUp";

export const Sign = () => {
  const [signed, setSigned] = useState(true);

  return signed ? (
    <SignIn onSwitch={() => setSigned(false)} />
  ) : (
    <SignUp onSwitch={() => setSigned(true)} />
  );
};
