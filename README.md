# Minecraft Status Bot

A clean and lightweight Discord bot that displays the live status of any Minecraft server using the JDA framework and the MCSrvStat API.

## Features

* **Live Status:** Instantly checks if a server is online or offline.
* **Player Count:** Displays current and maximum player slots.
* **Server Version:** Shows the specific Minecraft version the server is running on.
* **In-Memory Icons:** Decodes and attaches server icons directly from memory without saving files to the disk.

## Installation

1. **Clone the project:**
   ```bash
   git clone https://github.com/steffenfsp05/Discord-Basics.git
   cd Discord-Basics

2. **Build with Maven:**
   ```bash
   mvn clean install

3. **Run:**
   Add your Discord Bot token to the main class and execute `org.pytenix.MinecraftStatusBot`.

## Usage

Use the slash command in your Discord server:

`/status <server-ip>`

*Example: `/status mc.hypixel.net`*
