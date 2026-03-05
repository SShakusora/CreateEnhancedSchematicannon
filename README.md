# Create: Enhanced Schematicannon

An addon for the **Create** mod that bridges the gap between the Schematicannon and other mods. This mod ensures that complex blocks, block entities, and multi-part structures from various mods can be printed accurately.

---

## Supported Versions

| **Minecraft Version** | **Create Version Compatibility** |
| ----------------------------- | ---------------------------------------- |
| **1.21.1**                 | **Create 6.0+**                   |
| **1.20.1**                 | **Create 0.5.1j / 6.0+**          |

---

## About the Mod

In the base **Create** mod, the **Schematicannon** is a powerful tool for automated construction. However, it often struggles when encountering blocks from other mods—especially those with complex NBT data, custom rendering, or multi-block structures.

**Create: Enhanced Schematicannon** is designed to fix these limitations. It enhances the printing logic to support a wider range of third-party mod blocks, making your automated building process truly "mod-compatible."

---

## Showcase

### Accurate Material Checklist for AE2

![Accurate Material Checklist for AE2](https://github.com/SShakusora/CreateEnhancedSchematicannon/blob/1.21.1/main/Showcase/ae2_right_checklist.gif?raw=true)

### Seamless AE2 Cable Printing

![Seamless AE2 Cable Printing](https://github.com/SShakusora/CreateEnhancedSchematicannon/blob/1.21.1/main/Showcase/ae2_cable_print.gif?raw=true)

### Transformation Support

![Transformation Support](https://github.com/SShakusora/CreateEnhancedSchematicannon/blob/1.21.1/main/Showcase/ae2_mirror_cable_print.gif?raw=true)

---

## Key Features

* ​**Block Entity Sync (Server-Side Data Retrieval)​**: Standard `Schematic and Quill` typically extracts NBT data from the client side, which can result in missing information for certain Block Entities. This mod introduces a **Block Entity Sync** feature that requests data directly from the server, ensuring the generated `.nbt` files are complete and accurate.

  ![Block Entity Sync](https://github.com/SShakusora/CreateEnhancedSchematicannon/blob/1.21.1/main/Showcase/blockentity_sync.png?raw=true)
* ​**Enhanced Printing Logic**​: Prevents NBT data loss when the Schematicannon places blocks from other mods by ensuring proper data restoration.
* ​**Multi-Part Compatibility**​: Fixes issues where blocks with complex collision boxes or multiple parts fail to be placed correctly.
* ​**Deep Integration**​: Specifically targets compatibility gaps between Create and popular decoration/technical mods.

---

## Supported Mods

Below is a list of mods that receive enhanced support through this addon:

| **Mod**                                                                                        | **Description**                                                 |
|------------------------------------------------------------------------------------------------| ------------------ |
| [*Applied Energistics 2* ](https://www.curseforge.com/minecraft/mc-mods/applied-energistics-2) | *Fully Compatible: Cables and BlockEntities now print correctly with full data.*                   |
| [*Integrated Dynamics*](https://www.curseforge.com/minecraft/mc-mods/integrated-dynamics)      | *Fully Compatible: Logic Cables and parts now print correctly.*      |
| [*Functional Storage*](https://www.curseforge.com/minecraft/mc-mods/functional-storage)        | *Fully Compatible: Drawers and Controllers print correctly while preserving configurations.*      |
| [*AnvilCraft*](https://www.curseforge.com/minecraft/mc-mods/anvilcraft)                   | *Partial Support: Some BlockEntity now print correctly.*      |
| [*Supplementaries*](https://www.curseforge.com/minecraft/mc-mods/supplementaries)              | *Partial Support: Some BlockEntity now print correctly.*      |
| [*Amendments*](https://www.curseforge.com/minecraft/mc-mods/amendments)                        | *Partial Support: Some BlockEntity now print correctly.*      |
| [*Quark*](https://www.curseforge.com/minecraft/mc-mods/quark)                                  | *Partial Support: Some BlockEntity now print correctly.*      |
| [*Dramatic Doors*](https://www.curseforge.com/minecraft/mc-mods/dramatic-doors)                | *Fully Compatible: Fixed the placement logic for tall and special door types.*      |
| [*Chimes*](https://www.curseforge.com/minecraft/mc-mods/chimes)                                | *Fully Compatible: Fixed the positioning and attachment of all wind chimes.*      |

If you encounter a specific mod block that still doesn't print correctly, please report it in the [**Issues**](https://github.com/SShakusora/CreateEnhancedSchematicannon/issues) section!

---

## Installation

1. Ensure you have **Minecraft Forge/NeoForge** installed.
2. ​Install the required dependency: [Create](https://www.curseforge.com/minecraft/mc-mods/create).
   ​
3. Drop the `.jar` file into your `mods` folder.
4. Launch the game and enjoy seamless printing!

---

## Contributing & Feedback

Contributions are welcome! If you encounter a block that doesn't print correctly, please open an issue.

* ​**Source Code**​: [GitHub Repository](https://github.com/SShakusora/CreateEnhancedSchematicannon)
* ​**License**​: Licensed under [MIT](https://github.com/SShakusora/CreateEnhancedSchematicannon/blob/main/LICENSE.txt).
* **Issue Tracker**​: Report bugs [here](https://github.com/SShakusora/CreateEnhancedSchematicannon/issues)

---

### Support the Project

If this mod helped you build your world, please consider giving a **Star** on [GitHub](https://github.com/SShakusora/CreateEnhancedSchematicannon)! It helps more people find the project.

