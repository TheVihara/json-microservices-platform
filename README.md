# nats-microservices-platform

## Overview

**nats-microservices-platform** is a modular, scalable microservices platform built with **Java** and **Kotlin** for distributed systems and Minecraft server integration. Designed to accelerate backend development, it features **NATS messaging**, **Protocol Buffers (protobuf)** serialization, and a robust, extensible architecture. The platform supports both cloud-native deployments and game server environments (Bukkit, Velocity).

## Key Features

- **Modular Microservices Architecture:** Build, deploy, and scale independent Java/Kotlin services easily.
- **NATS Messaging Integration:** High-performance, event-driven communication between services using [NATS](https://nats.io/).
- **Protocol Buffers:** Efficient, type-safe data serialization using [Google Protocol Buffers](https://developers.google.com/protocol-buffers).
- **Minecraft Server Support:** Out-of-the-box integration with **Bukkit** and **Velocity** for Minecraft plugin and proxy development.
- **Extensible and Configurable:** Easily add new services and customize configurations per module.
- **Cloud-Native Ready:** Supports scalable deployments, configuration management, and robust service orchestration.
- **Automated Testing:** Built-in support for **JUnit 5** for comprehensive unit and integration testing.

## Technologies Used

- **Java 17+** & **Kotlin**
- **NATS (jnats)**
- **Protocol Buffers (protobuf-java, protoc)**
- **Gradle (Kotlin DSL)**
- **JUnit 5**
- **LuckPerms** (for permissions)
- **jshepherd** (configuration)
- **Bukkit** and **Velocity** (Minecraft APIs)

## Main Modules

- **common**: Shared utilities, service abstractions, and configuration.
- **service:auth**: Authentication and security service.
- **service:party**: Party/group management.
- **service:friend**: Social/friends features.
- **service:permission**: Permissions management (LuckPerms integration).
- **service:punishment**: Moderation and punishment service.
- **service:deploy**: Deployment/orchestration tools.
- **service:player**: Player profile and management.
- **service:command**: Command processing and registration.
- **minecraft:bukkit**: Bukkit Minecraft server integration.
- **minecraft:velocity**: Velocity Minecraft proxy integration.

## Getting Started

1. **Clone the Repository**
   ```sh
   git clone https://github.com/TheVihara/nats-microservices-platform.git
   cd nats-microservices-platform
   ```

2. **Build the Project**
   ```sh
   ./gradlew build
   ```

3. **Configure Services**
   - Each service uses a `service.yml` configuration file.
   - See `service/common/src/main/java/net/unnamed/service/common/config/ServiceConfig.java` for required options.

4. **Run a Service**
   - Use the provided main classes in each module to start individual services.

## Why nats-microservices-platform?

- **Optimized for Java/Kotlin Microservices:** Built for modern JVM development with seamless NATS and protobuf integration.
- **Production-Ready Distributed Systems:** Designed for scalability, reliability, and extensibility.
- **Minecraft Server Friendly:** Plug-and-play modules for Bukkit and Velocity Minecraft environments.
- **Cloud-Native Architecture:** Supports containerization, orchestration, and scalable deployments.
- **Active Development:** Modular design makes contribution and extension easy.

## Contributing

We welcome contributions! Fork the repo and open a pull request.

## License

MIT License

---

*Built and maintained by [TheVihara](https://github.com/TheVihara).*

---

**SEO Keywords:**  
java, kotlin, microservices, distributed-systems, nats, protocol-buffers, protobuf, modular-architecture, backend, service-platform, minecraft, bukkit, velocity, game-server, cloud-native, event-driven, gradle, junit, extensible, scalable
