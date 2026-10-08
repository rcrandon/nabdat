# Nab 'Dat for android

A phone-native port of [pablostanley/yoinks](https://github.com/pablostanley/yoinks), renamed:
paste a link (or share one from any app), pick a resolution or audio-only mp3, done.
YouTube, X, Instagram, Threads, TikTok and the 1,800+ other sites yt-dlp supports.

This folder started as a copy of `../Yoinks` (yoinks for android 1.0.1). It has its own app id,
`app.nabdat`, and its own signing key, so it installs next to yoinks instead of over it, and
the two never share history, settings or downloads.

Everything ships inside the APK, so it works out of the box with no Termux, no Python
install and no setup:

| piece   | what it does                                    | source                              |
|---------|-------------------------------------------------|-------------------------------------|
| yt-dlp  | the extractor (bundled 2026.08.19, self-updates) | `app/src/main/res/raw/ytdlp`        |
| python  | runs yt-dlp (3.12)                              | youtubedl-android 0.18.1            |
| ffmpeg  | merges hi-res video+audio, makes mp3s           | youtubedl-android ffmpeg 0.18.1     |
| QuickJS | solves YouTube's JavaScript challenges          | youtubedl-android 0.18.1            |

## Using it

- **Share** a video from YouTube, Instagram, TikTok, X, a browser... and pick **Nab 'Dat**.
- Or open Nab 'Dat and paste. A copied link shows up as "link in your clipboard: tap to nab it".
- Or select a link anywhere and choose **Nab 'Dat** from the text menu.
- Files land in `Download/Nab 'Dat` and show up in Gallery, Files and music apps.
- Downloads keep going in the background, with progress and a cancel button in the notification.
- `◐ theme` cycles auto / light / dark, same palettes as the CLI. `? about` shows engine
  versions and an "update yt-dlp" button (it also checks by itself daily, and again whenever
  a site stops working).

## The logo

The wordmark spells `NAB 'DAT` in the pixel font of the yoinks CLI logo: its N, plus A, B, ',
D and T drawn to match, each letter dithering one stroke of its middle band. The intro flicker
and the shimmer beam are unchanged. The launcher, themed, splash and notification icons are that
same N, the way the yoinks icon was the Y of its wordmark.

## Installing on the phone (Galaxy S22)

1. Copy `dist/nabdat-1.0.0.apk` to the phone (WhatsApp, Drive, USB, anything).
2. Tap it. Android asks to allow installs from that app once: allow, then **Install**.
3. If Play Protect warns about an unknown developer, choose **More details → Install anyway**
   (it's signed with your own key, not a Play Store key).

## Rebuilding ("repacking")

Needs the Android SDK (Android Studio) and JDK 17. From this folder:

```powershell
.\build.ps1                 # dist\nabdat-<version>.apk, arm64 for phones
.\build.ps1 -RefreshYtDlp   # bundle the newest yt-dlp first
.\build.ps1 -Emulator       # also include x86_64 for a desktop emulator
.\build.ps1 -Install        # build and adb-install onto a plugged-in phone
```

To capture the log, redirect outside PowerShell (`cmd /c "powershell -File build.ps1 > build-log.txt 2>&1"`).
Redirecting with `*>` inside PowerShell turns the JDK's "Picked up JAVA_TOOL_OPTIONS" stderr line
into an error, and the script stops on it.

The release key lives in `keystore/` (git-ignored). Keep it: Android only installs an
update over an existing Nab 'Dat if it's signed with the same key. Bump `versionCode` /
`versionName` in `app/build.gradle.kts` for each release.

## Layout

```
app/src/main/java/app/nabdat/
  engine/   Engine.kt (yt-dlp lifecycle, probe, download, self-update)
            Choices.kt / Format.kt / Platforms.kt (ported 1:1 from the CLI's lib/)
  data/     MediaSaver (MediaStore → Download/Nab 'Dat), History, Prefs
  ui/       Logo (the pixel wordmark + shimmer), Components, Screens, Theme
  Nabber.kt           the input → probing → picking → downloading → done state machine
  DownloadService.kt  foreground service + notifications
```

Licenses: yoinks is MIT; youtubedl-android is GPL-3.0 (so this build is GPL-3.0);
yt-dlp is Unlicense; JetBrains Mono is OFL (see `licenses-JetBrainsMono-OFL.txt`).
