import { MockStorage } from "../features/common/state/MockStorage";
import { LocalStorage } from "../features/common/state/LocalStorage";
import { createUsePersistantStorageValueHook } from "../features/common/state/createUsePersistentStorageValueHook";

export enum PersistentStorageKey {
  ThemePreference = "themePreference",
  Promotion = "promotion",
}

export const persistentStorage =
  typeof window !== "undefined"
    ? new LocalStorage<PersistentStorageKey>()
    : new MockStorage<PersistentStorageKey>();

export const usePersistentStorageValue =
  createUsePersistantStorageValueHook<PersistentStorageKey>(persistentStorage);
