import { spawnSync } from "child_process";
import { defineConfig } from "vite";

function isDev() {
  return process.env.NODE_ENV !== "production";
}

const replacementForPublic = isDev()
  ? "./out/app/fastLinkJS.dest"
  : "./out/app/fullLinkJS.dest"

export default defineConfig({
  resolve: {
    alias: [
      {
        find: "@public",
        replacement: replacementForPublic,
      },
    ],
  },
});
