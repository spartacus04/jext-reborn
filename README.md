# Jukebox Extended Reborn | Web UI

>The webui allows the user to configure the plugin in the browser with ease

### Features

- Disc management
- Resource pack merging
- Apply discs and resourcepack directly on the server
- Remote configuration
- Documentation for both users and developers
- Desktop app for faster disc management

### How to run

You can either open the https://spartacus04.github.io/jext-reborn/ webpage or build it yourself locally.

##### Building locally

First git clone the repo:

```
git clone https://github.com/spartacus04/jext-reborn
git checkout gh-pages
```

Then install the dependencies (here i'm using bun, you can use any node package manager):

```
bun install
```

You can run a live preview by running `bun run dev` or you can build the website by running `bun run build`

You can run a live preview of the desktop app by running `bun run tauri dev` (note: this requires rust), or you can build it by running `bun run tauri build`

