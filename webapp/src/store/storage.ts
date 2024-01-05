import { create } from "zustand";
import { devtools } from "zustand/middleware";

export interface FoldersStore {
  folder: string;
  setFolder: (folder: string) => void;
}

export const useFoldersStore = create<FoldersStore>()(
  devtools((set) => ({
    folder: "/",

    setFolder: (folder: string) => {
      set((state) => ({ ...state, folder: folder }));
    },
  }))
);
