import { create } from "zustand";

export interface FoldersStore {
  path: string;

  setPath: (path: string) => void;
}

export const useFoldersStore = create<FoldersStore>()((set) => ({
  path: "/",

  setPath: (path: string) => {
    set((state) => ({ ...state, path: path }));
  },
}));
