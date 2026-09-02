# Physics

[English](README.md) | [简体中文](README_zh.md)

A reference application for PICO OS 6 demonstrating physics simulation capabilities with the PICO Spatial SDK, showcasing rigid body dynamics, collision detection, and collision events.

## Overview

Physics Sample provides an interactive 3D environment where users can interact with physics-enabled objects and observe realistic physical behaviors. It serves as a practical guide for setting rigid body properties, configuring collision detection, and leveraging collision callbacks using the Spatial SDK.

![Screenshot](.github/screenshot.jpeg)

## Features

Physics Sample highlights the following core physics capabilities of the PICO Spatial SDK:

- **Interactive Physics Simulation**: real-time physics interactions with rigid body dynamics
- **Collision Detection & Response**: advanced collision systems for realistic object interactions
- **Domino Chain Reactions**: complex physics chain reactions demonstrated with dominoes
- **Realistic Object Behavior**: gravity, momentum, friction, and other physical properties
- **Dynamic Material Change**: collision callbacks that dynamically change domino materials on impact

## Requirements

To build and run this project, configure your development environment by following the official PICO Spatial SDK setup guide:

- [PICO Spatial SDK Setup Guide (Outside Chinese Mainland)](https://developer.picoxr.com/document/spatial-sdk/set-up-development-environment/)
- [PICO Spatial SDK Setup Guide (Chinese Mainland)](https://developer-cn.picoxr.com/document/spatial-sdk/set-up-development-environment/)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Physics
```

### 2. Open in Android Studio

Launch Android Studio, select **Open an Existing Project**, and choose the `Physics` directory.

### 3. Run the App

#### On Emulator

1. Open **Device Manager** (toolbar or **Tools > Device Manager**)
2. Click **Add a new device** > **Create a PICO Emulator**
3. Start the emulator from **Device Manager**
4. Click **Run 'app'** to launch the app

#### On Physical Device

1. Connect your PICO device to your development machine
2. Enable **USB Debugging** on the device
3. Click **Run 'app'** to deploy directly to your device

## Project Structure

Physics Sample follows a modular architecture to separate application logic from 3D resources:

```txt
Physics/
├── app/                                  # Main application module
│   └── src/main/
│       ├── java/com/pico/spatial/sample/physics/ # Kotlin source files
│       │   ├── Main.kt                     # App entry point: sets up the UI container and loads assets
│       │   ├── data/                       # Data management
│       │   │   └── AssetBundle.kt          # Async loading of 3D scene bundles
│       │   ├── ecs/                        # Entity Component System implementation
│       │   │   └── Domino.kt               # Domino component with index tracking
│       │   ├── manager/                    # Physics and event management
│       │   │   ├── PhysicsManager.kt       # Core physics: scene loading, collision setup, rigid body dynamics
│       │   │   └── EventManager.kt         # Collision event subscription and unsubscription
│       │   ├── platform/                   # Platform-specific implementations
│       │   └── ui/                         # UI components and ViewModel
│       │       ├── MainScreen.kt           # Main UI with spatial view and control button
│       │       └── PhysicsViewModel.kt     # Manages play phases and coordinates physics operations
│       ├── assets/                         # App assets
│       ├── res/                            # Android resources
│       └── AndroidManifest.xml             # App manifest with Spatial component declarations
├── editor-asset/                         # 3D resources and physics scene definitions
│   └── src/main/res3d/Sample_physics/    # Spatial Editor physics project
├── gradle/                               # Gradle dependencies and scripts
├── build.gradle.kts                      # Root build configuration
└── settings.gradle.kts                   # Project settings and SDK configuration
```

## Editing 3D Assets

To modify or extend the physics-enabled 3D assets in Physics Sample:

1. In Android Studio, expand the **editor-asset** module
2. Navigate to **res3d/Sample_physics/ModelView**
3. Click **Open in Editor** in the top-right corner to launch the PICO Spatial Editor
4. Use the editor to adjust the physics scene layout, modify domino configurations, or add new physics-enabled objects

![Spatial Editor](.github/editor.png)

## Learning Resources

To deepen your understanding of spatial app development, explore the resources on our official developer websites:

- [PICO Developer Website (Outside Chinese Mainland)](https://developer.picoxr.com/)
- [PICO Developer Website (Chinese Mainland)](https://developer-cn.picoxr.com/)

## License

© 2026 PICO. The project's source code is licensed under the [Apache 2.0 License](LICENSE.txt). All assets, including textures, 3D models, audio, and others, are licensed via [Creative Commons BY 4.0](https://creativecommons.org/licenses/by/4.0/).

## Support

For questions, issues, or feature requests, please contact us through the [PICO Developer Support Portal](https://picodevsupport.freshdesk.com/support/home) and submit a ticket.

---

Developed as a reference for spatial application developers
