// vite.config.ts
import { defineConfig } from "file:///home/andrea/source/jext-tool/node_modules/.pnpm/vite@4.1.3_hlkwzk2izwsolfmdrejei4vrty/node_modules/vite/dist/node/index.js";
import { svelte } from "file:///home/andrea/source/jext-tool/node_modules/.pnpm/@sveltejs+vite-plugin-svelte@2.0.2_svelte@3.55.1+vite@4.1.3/node_modules/@sveltejs/vite-plugin-svelte/dist/index.js";
import autoprefixer from "file:///home/andrea/source/jext-tool/node_modules/.pnpm/autoprefixer@10.4.13_postcss@8.4.21/node_modules/autoprefixer/lib/autoprefixer.js";
import path from "node:path";
var __vite_injected_original_dirname = "/home/andrea/source/jext-tool";
var vite_config_default = defineConfig({
  build: {
    outDir: "docs"
  },
  base: "/jext-reborn/",
  plugins: [svelte()],
  css: {
    postcss: {
      plugins: [
        autoprefixer
      ]
    }
  },
  resolve: {
    alias: [
      { find: "@", replacement: path.resolve(__vite_injected_original_dirname, "./src") },
      { find: "@assets", replacement: path.resolve(__vite_injected_original_dirname, "./src/assets") },
      { find: "@styles", replacement: path.resolve(__vite_injected_original_dirname, "./src/styles") },
      { find: "@lib", replacement: path.resolve(__vite_injected_original_dirname, "./src/lib") },
      { find: "@ui", replacement: path.resolve(__vite_injected_original_dirname, "./src/ui") }
    ]
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCIvaG9tZS9hbmRyZWEvc291cmNlL2pleHQtdG9vbFwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiL2hvbWUvYW5kcmVhL3NvdXJjZS9qZXh0LXRvb2wvdml0ZS5jb25maWcudHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL2hvbWUvYW5kcmVhL3NvdXJjZS9qZXh0LXRvb2wvdml0ZS5jb25maWcudHNcIjtpbXBvcnQgeyBkZWZpbmVDb25maWcgfSBmcm9tICd2aXRlJztcbmltcG9ydCB7IHN2ZWx0ZSB9IGZyb20gJ0BzdmVsdGVqcy92aXRlLXBsdWdpbi1zdmVsdGUnO1xuaW1wb3J0IGF1dG9wcmVmaXhlciBmcm9tICdhdXRvcHJlZml4ZXInO1xuaW1wb3J0IHBhdGggZnJvbSAnbm9kZTpwYXRoJztcblxuZXhwb3J0IGRlZmF1bHQgZGVmaW5lQ29uZmlnKHtcblx0YnVpbGQ6IHtcblx0XHRvdXREaXI6ICdkb2NzJyxcblx0fSxcblx0YmFzZTogJy9qZXh0LXJlYm9ybi8nLFxuXG5cdHBsdWdpbnM6IFtzdmVsdGUoKV0sXG5cdGNzczoge1xuXHRcdHBvc3Rjc3M6IHtcblx0XHRcdHBsdWdpbnM6IFtcblx0XHRcdFx0YXV0b3ByZWZpeGVyLFxuXHRcdFx0XSxcblx0XHR9LFxuXHR9LFxuXG5cdHJlc29sdmU6IHtcblx0XHRhbGlhczogW1xuXHRcdFx0eyBmaW5kOiAnQCcsIHJlcGxhY2VtZW50OiBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCAnLi9zcmMnKSB9LFxuXHRcdFx0eyBmaW5kOiAnQGFzc2V0cycsIHJlcGxhY2VtZW50OiBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCAnLi9zcmMvYXNzZXRzJykgfSxcblx0XHRcdHsgZmluZDogJ0BzdHlsZXMnLCByZXBsYWNlbWVudDogcGF0aC5yZXNvbHZlKF9fZGlybmFtZSwgJy4vc3JjL3N0eWxlcycpIH0sXG5cdFx0XHR7IGZpbmQ6ICdAbGliJywgcmVwbGFjZW1lbnQ6IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsICcuL3NyYy9saWInKSB9LFxuXHRcdFx0eyBmaW5kOiAnQHVpJywgcmVwbGFjZW1lbnQ6IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsICcuL3NyYy91aScpIH1cblx0XHRdXG5cdH1cbn0pO1xuIl0sCiAgIm1hcHBpbmdzIjogIjtBQUF5USxTQUFTLG9CQUFvQjtBQUN0UyxTQUFTLGNBQWM7QUFDdkIsT0FBTyxrQkFBa0I7QUFDekIsT0FBTyxVQUFVO0FBSGpCLElBQU0sbUNBQW1DO0FBS3pDLElBQU8sc0JBQVEsYUFBYTtBQUFBLEVBQzNCLE9BQU87QUFBQSxJQUNOLFFBQVE7QUFBQSxFQUNUO0FBQUEsRUFDQSxNQUFNO0FBQUEsRUFFTixTQUFTLENBQUMsT0FBTyxDQUFDO0FBQUEsRUFDbEIsS0FBSztBQUFBLElBQ0osU0FBUztBQUFBLE1BQ1IsU0FBUztBQUFBLFFBQ1I7QUFBQSxNQUNEO0FBQUEsSUFDRDtBQUFBLEVBQ0Q7QUFBQSxFQUVBLFNBQVM7QUFBQSxJQUNSLE9BQU87QUFBQSxNQUNOLEVBQUUsTUFBTSxLQUFLLGFBQWEsS0FBSyxRQUFRLGtDQUFXLE9BQU8sRUFBRTtBQUFBLE1BQzNELEVBQUUsTUFBTSxXQUFXLGFBQWEsS0FBSyxRQUFRLGtDQUFXLGNBQWMsRUFBRTtBQUFBLE1BQ3hFLEVBQUUsTUFBTSxXQUFXLGFBQWEsS0FBSyxRQUFRLGtDQUFXLGNBQWMsRUFBRTtBQUFBLE1BQ3hFLEVBQUUsTUFBTSxRQUFRLGFBQWEsS0FBSyxRQUFRLGtDQUFXLFdBQVcsRUFBRTtBQUFBLE1BQ2xFLEVBQUUsTUFBTSxPQUFPLGFBQWEsS0FBSyxRQUFRLGtDQUFXLFVBQVUsRUFBRTtBQUFBLElBQ2pFO0FBQUEsRUFDRDtBQUNELENBQUM7IiwKICAibmFtZXMiOiBbXQp9Cg==
