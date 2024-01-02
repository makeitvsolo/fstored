import { create } from "zustand";

export interface FoldersStore {
  folder: string;
  setFolder: (folder: string) => void;
}

export const useFoldersStore = create<FoldersStore>()((set) => ({
  folder: "/",

  setFolder: (folder: string) => {
    set((state) => ({ ...state, folder: folder }));
  },
}));
