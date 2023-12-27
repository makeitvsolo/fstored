import { create } from "zustand";
import { createJSONStorage, persist } from "zustand/middleware";

export type User = {
  id: string;
  name: string;
};

export interface UserStore {
  activeUser: User | null;

  setActiveUser: (activeUser: User) => void;
  removeActiveUser: () => void;
}

export const useUserStore = create<UserStore>()(
  persist(
    (set) => ({
      activeUser: null,

      setActiveUser: (activeUser: User) => {
        set((state) => ({ ...state, activeUser: activeUser }));
      },

      removeActiveUser: () => {
        set((state) => ({ ...state, activeUser: null }));
      },
    }),

    {
      name: "fstored-user",
      storage: createJSONStorage(() => sessionStorage),
    }
  )
);
