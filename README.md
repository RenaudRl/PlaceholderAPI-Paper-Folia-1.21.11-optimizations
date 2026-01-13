# PlaceholderAPI

![Java Version](https://img.shields.io/badge/Java-21-orange)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Target](https://img.shields.io/badge/Target-Folia%20/%20Paper-blue)

**PlaceholderAPI** is a high-performance, strictly optimized fork of **ExtendedClip's PlaceholderAPI**, engineered specifically for the **BTC Studio** infrastructure. This fork drops support for legacy platforms (Spigot, Bukkit, older NMS) to provide native, blazingly fast integration with **Paper** and **Folia**.

> [!WARNING]
> **PLATFORM COMPATIBILITY NOTICE**
> This fork is **STRICTLY** for Paper 1.21.11+ and Folia 1.21.11+. Legacy compatibility layers have been removed to maximize performance. If you are not running modern Paper/Folia, this plugin **will not function**.

---

## 🚀 Key Features in Detail

### ⚡ Concurrency & Threading (Folia Native)
- **Hardcoded Folia Scheduler**: Deeply integrated `FoliaScheduler` ensures that all tasks (Global & Region-synced) are handled correctly without the overhead of dynamic platform detection.
- **Zero-Overhead Logic**: Slashed unnecessary logic checks for non-Folia platforms, resulting in faster tick-to-task execution.

### 🛠️ Core Optimisations & Debloating
- **Java 21 Native**: Leveraging the latest JVM optimizations for maximum throughput and memory efficiency.
- **Legacy Cleanup**: Removed over a decade of legacy compatibility code (NMS 1.7 - 1.20) and Spigot-specific update checkers.
- **Internalised Expansions**: The `Math` and `Formatter` expansions are now built-in, eliminating external dependency overhead and ensuring 100% compatibility.

### 🌍 Deployment & Startup
- **Steamlined Loading**: Faster startup times through optimized expansion discovery and reduced library dependencies.
- **Plug & Play**: Automatic threading context detection for both Paper and Folia environments.

---

## ⚙️ Configuration

PlaceholderAPI is primarily tuned via `config.yml`.

### Key Settings
| Key | Default | Description |
|-----|---------|-------------|
| `check_updates` | `true` | Enables/Disables internal update checks. |
| `cloud_enabled` | `true` | Enables access to the Expansion Cloud. |
| `boolean.true` | `yes` | Global representation of "true" values. |
| `detect_malicious_expansions` | `true` | Built-in security check for third-party expansions. |

---

## 🛠 Building & Deployment

Requires **Java 21**.

```bash
# Clean and compile the project
./gradlew clean build
```

---

## 🤝 Credits & Inspiration
This project is built upon the innovation of the broader Minecraft development community:
- **[PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI)** - The original project by ExtendedClip.

---

## 📜 License
- **Custom BTC-CORE Patches**: Proprietary to **BTC Studio**.
- **Upstream Source**: Original licenses apply to their respective components from PlaceholderAPI.

---
**Fork maintained by BTCSTUDIO**
