# Capsule 💊

**Battery-optimized status bar time utility for Android.**

Built with **Jetpack Compose** and **Material 3**. Designed for performance, geometric precision, and minimal system footprint.

---

## 🚀 Core Features & Customization
- **Pixel-Perfect Geometry:** Independent control for all 4 Corner Radii (0-100px), Pill Width (50-300dp), and Height (20-150dp). 
- **Dynamic Surface Modes:** Choose between **Solid** fills or **Linear Gradients** (Vertical/Horizontal) with independent Light/Dark mode color pairs.
- **M3 Token Integration:** Native support for Material 3 Color Tokens (Primary, Surface, etc.) or fully custom Hex codes.
- **Advanced Text Styling:** Toggleable Drop Shadows, font weight adjustments (Normal/Medium/Bold), and real-time font scaling (10-40sp).
- **Format Flexibility:** 24-Hour/12-Hour switching with optional AM/PM markers and toggleable high-frequency seconds.
- **Fluid Morphing UI:** Every adjustment is applied with spring-physics animations; no snapping or layout jumping.

---

## 🛠 Tech Stack
- **UI:** Jetpack Compose (Material 3)
- **State Management:** Reactive Compose State + SharedPreferences
- **Architecture:** Clean Component-based UI with persistent `PrefsManager`.
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34+

---

## 📊 Performance & Battery Diagnostic
This project includes a built-in **Diagnostic Report** to verify system impact.

| Metric | Result | Impact |
| :--- | :--- | :--- |
| **Idle Delta** | < 3mA | Negligible battery drain |
| **RAM Usage** | ~12MB - 18MB | Ultra-light memory footprint |
| **CPU Time** | < 1s / hour | Efficient sleep-state handling |
| **Rendering** | 60/90/120Hz | Zero-jank animations (HWUI Optimized) |

> **Note:** To perform a "Delta Test," check **System Draw** in the Diagnostic Card with the pill ON vs. OFF.

---

## 📦 Installation & Setup
1. **Clone:** `git clone https://github.com/myselfkshitiz/Capsule.git`
2. **Build:** Open in AndroidIDE or Android Studio and run `./gradlew assembleDebug`.
3. **Permissions:** Requires `Overlay Permission` and `Notification Access` for status bar persistence.

---

## 💾 Backup & Restore
Settings are stored in `.capsule` (JSON-based) format.
* **Export:** Saves current geometry, colors, and format configurations.
* **Import:** Instant UI refresh via `importTrigger` reactive key.

---

## 📜 License
Licensed under the **MIT License**. See [LICENSE](LICENSE) for details.

---

**Developed by [Kshitiz](https://github.com/myselfkshitiz)** *Performance-first Android Utilities.*

