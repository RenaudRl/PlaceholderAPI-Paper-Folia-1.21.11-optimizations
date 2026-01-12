# PlaceholderAPI (Paper/Folia 1.21.11+ Fork)

> [!IMPORTANT]
> This is a **strict fork** of PlaceholderAPI designed exclusively for **Paper 1.21.11+** and **Folia 1.21.11+**.
> It drops support for all other platforms (Spigot, Bukkit, older NMS versions) to maximize performance and compatibility with modern server software.

## Features & Optimizations

- **Folia Native**: Hardcoded `FoliaScheduler` integration ensuring all tasks (Global & Region-synced) are handled correctly without overhead logic.
- **Java 21**: Compiled for Java 21 to leverage the latest JVM optimizations and language features.
- **Debloated**: Slashed legacy compatibility code, removing NMS version checks (1.7 - 1.20) and Spigot-specific update checkers.
- **Optimized**: Streamlined startup and expansion loading for modern environments.

## Requirements

- **Java**: JDK 21 or higher.
- **Server**: Paper 1.21.11+ or Folia 1.21.11+.
- **Version**: Strictly targets `1.21.11` (or latest 1.21.11+ ecosystem).

## Build

```bash
./gradlew clean build
```

## Usage

Drop the jar into your `plugins` folder. No extra configuration needed for Folia; the plugin automatically detects and utilizes the correct threading context.

---
*Fork maintained by BTCSTUDIO*
