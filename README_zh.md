# Physics

[English](README.md) | [简体中文](README_zh.md)

Physics 是一款基于 PICO OS 6 的参考示例应用，使用 PICO Spatial SDK 演示物理仿真能力，涵盖刚体动力学、碰撞检测与碰撞事件。

## 概览

Physics 示例提供了一个交互式 3D 环境，用户可以与启用物理效果的对象进行交互，观察真实的物理行为。本示例是开发者的实践参考，演示了如何使用 Spatial SDK 设置刚体属性、配置碰撞检测以及运用碰撞回调。

![Screenshot](.github/screenshot.jpeg)

## 功能特性

Physics 示例展示了 PICO Spatial SDK 的以下核心物理能力：

- **交互式物理仿真**：基于刚体动力学的实时物理交互
- **碰撞检测与响应**：实现真实对象交互的高级碰撞系统
- **多米诺链式反应**：通过多米诺骨牌演示复杂的物理链式反应
- **真实物体行为**：重力、动量、摩擦力等物理属性的仿真
- **动态材质切换**：通过碰撞回调在碰撞发生时动态更换多米诺材质

## 环境要求

构建和运行本项目前，请参照 PICO Spatial SDK 官方文档完成开发环境配置：

- [PICO Spatial SDK 环境配置指南（中国大陆）](https://developer-cn.picoxr.com/document/spatial-sdk/set-up-development-environment/)
- [PICO Spatial SDK 环境配置指南（非中国大陆）](https://developer.picoxr.com/document/spatial-sdk/set-up-development-environment/)

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd Physics
```

### 2. 在 Android Studio 中打开项目

启动 Android Studio，选择 **Open an Existing Project**，然后选择 `Physics` 目录。

### 3. 运行项目

#### 在模拟器上运行

1. 打开 **Device Manager**（可通过工具栏或 **Tools > Device Manager** 进入）
2. 点击 **Add a new device** > **Create a PICO Emulator**
3. 在 **Device Manager** 中启动模拟器
4. 点击 **Run 'app'** 启动应用

#### 在物理设备上运行

1. 将 PICO 设备连接至开发机
2. 在设备上启用 **USB 调试（USB Debugging）**
3. 点击 **Run 'app'** 将应用直接部署到设备

## 项目结构

Physics 示例采用模块化架构，将应用逻辑与 3D 资源分离：

```txt
Physics/
├── app/                                  # 主应用模块
│   └── src/main/
│       ├── java/com/pico/spatial/sample/physics/ # Kotlin 源代码
│       │   ├── Main.kt                     # 应用入口：初始化 UI 容器并加载资源
│       │   ├── data/                       # 数据管理
│       │   │   └── AssetBundle.kt          # 异步加载 3D 场景包
│       │   ├── ecs/                        # 实体组件系统实现
│       │   │   └── Domino.kt               # 多米诺组件，包含索引追踪
│       │   ├── manager/                    # 物理与事件管理
│       │   │   ├── PhysicsManager.kt       # 核心物理：场景加载、碰撞配置、刚体动力学
│       │   │   └── EventManager.kt         # 碰撞事件的订阅与取消订阅
│       │   ├── platform/                   # 平台相关实现
│       │   └── ui/                         # UI 组件与 ViewModel
│       │       ├── MainScreen.kt           # 主 UI，包含空间视图与控制按钮
│       │       └── PhysicsViewModel.kt     # 管理播放阶段并协调物理操作
│       ├── assets/                         # 应用资源
│       ├── res/                            # Android 资源
│       └── AndroidManifest.xml             # 包含 Spatial 组件声明的应用清单
├── editor-asset/                         # 3D 资源与物理场景定义
│   └── src/main/res3d/Sample_physics/    # Spatial Editor 物理项目
├── gradle/                               # Gradle 依赖与构建脚本
├── build.gradle.kts                      # 根构建配置
└── settings.gradle.kts                   # 项目设置与 SDK 配置
```

## 编辑 3D 资源

如需修改或扩展 Physics 示例中的物理 3D 资源，请按以下步骤操作：

1. 在 Android Studio 中展开 **editor-asset** 模块
2. 导航至 **res3d/Sample_physics/ModelView**
3. 点击右上角的 **Open in Editor** 启动 PICO Spatial Editor
4. 在编辑器中调整物理场景布局、修改多米诺配置或添加新的物理对象

![Spatial Editor](.github/editor.png)

## 学习资源

深入了解空间应用开发，欢迎访问官方开发者网站获取完整资源：

- [PICO 开发者网站（中国大陆）](https://developer-cn.picoxr.com/)
- [PICO 开发者网站（非中国大陆）](https://developer.picoxr.com/)

## 许可证

© 2026 PICO。本项目的源代码依据 [Apache 2.0 License](LICENSE.txt) 协议开源。本项目的美术资产（包括但不限于纹理、3D 模型和音频）基于 [Creative Commons BY 4.0](https://creativecommons.org/licenses/by/4.0/) 授权。

## 支持

如有任何问题、疑惑或功能建议，欢迎通过 [PICO 开发者支持门户](https://picodevsupport.freshdesk.com/support/home) 提交工单与我们联系。

---

为空间应用开发者提供的官方参考示例
