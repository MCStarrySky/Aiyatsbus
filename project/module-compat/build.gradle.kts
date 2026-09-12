repositories {
    maven("https://repo.momirealms.net/releases/")                           // CraftEngine 仓库
    maven("https://repo.papermc.io/repository/maven-public/")                 // Paper, PlotSquared
    maven("https://maven.enginehub.org/repo/")                                // WorldGuard
    maven("https://jitpack.io/")                                              // Lands, GriefPrevention, NoBuildPlus
    maven("https://nexus.iridiumdevelopment.net/repository/maven-releases/")  // IridiumSkyBlock
    maven("https://repo.codemc.org/repository/maven-public/")                 // BentoBox
    maven("https://repo.essentialsx.net/releases/")                           // EssentialsX
    maven("https://repo.glaremasters.me/repository/bloodshot/")               // GriefDefender
    maven("https://repo.william278.net/releases/")                            // HuskTowns, HuskClaims
    maven("https://repo.bg-software.com/repository/api/")                     // SuperiorSkyBlock
    maven("https://repo.glaremasters.me/repository/towny/")                   // Towny
    maven("https://repo.songoda.com/repository/minecraft-plugins/")           // FabledSkyBlock, UltimateClaims
    maven("https://ci.ender.zone/plugin/repository/everything/")              // FactionsUUID
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")     // RedProtect
    maven("https://eldonexus.de/repository/maven-releases/")                  // Landlord
    maven("https://nexus.phoenixdevt.fr/repository/maven-public/")            // MMOItems
    maven("https://r.irepo.space/maven/")                                     // NeigeItems
    maven("https://repo.oraxen.com/releases")                                 // Oraxen
    maven("https://repo.tabooproject.org/repository/releases")                // Zaphkiel
}

dependencies {
    // 服务端
    compileOnly("paper:v12104:12104:api")

    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly(project(":project:module-ingame"))

    compileOnly("world.bentobox:bentobox:2.1.0-SNAPSHOT")                         // BentoBox
    compileOnly("net.essentialsx:EssentialsX:2.19.7")                             // EssentialsX
    compileOnly("public:QuickShop-Hikari:6.2.0.6")                                // QuickShop-Hikari
    compileOnly("public:QuickShop-Reremake:5.1.2.5")                              // QuickShop-Reremake
    compileOnly("public:CitizensAPI:2.0.35")                                      // Citizens
    compileOnly("public:BlockLocker:1.13")                                        // BlockLocker
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")                   // WorldGuard
    compileOnly("com.mcstarrysky:land:1.0.1")                                     // Land
    compileOnly("com.github.angeschossen:LandsAPI:7.0.2")                         // Lands
    compileOnly("com.github.Ez4p1xEL:NoBuildPlus:1.5.52")                         // NoBuildPlus (新版)
    compileOnly("com.iridium:IridiumSkyblock:4.1.5")                              // IridiumSkyBlock
    compileOnly("world.bentobox:bentobox:2.1.0-SNAPSHOT")                         // BentoBox
    compileOnly("com.griefdefender:api:2.1.0-SNAPSHOT")                           // GriefDefender
    compileOnly("net.william278.husktowns:husktowns-bukkit:3.0.2")                // HuskTowns
    compileOnly("net.william278.huskclaims:huskclaims-bukkit:1.0.3")              // HuskClaims
    compileOnly("com.intellectualsites.plotsquared:plotsquared-bukkit:7.3.5")     // PlotSquared
    compileOnly("com.bgsoftware:SuperiorSkyblockAPI:2023.3")                      // SuperiorSkyBlock2 - API
    compileOnly("com.palmergames.bukkit.towny:towny:0.100.1.17")                  // Towny
    compileOnly("com.craftaro:FabledSkyBlock:3.0.4")                              // FabledSkyBlock
    compileOnly("com.craftaro:UltimateClaims:2.2.0")                              // UltimateClaims
    compileOnly("com.github.TechFortress:GriefPrevention:16.18.2")                // GriefPrevention
    compileOnly("com.massivecraft:Factions:1.6.9.5-U0.6.33")                      // FactionsUUID
    compileOnly("dev.espi:protectionstones:2.10.2")                               // ProtectionStones
    compileOnly("biz.princeps:landlord-core:4.364")                               // Landlord
    compileOnly("com.github.UlrichBR:UClans-API:8.8.0-R1")                        // UltimateClans
    compileOnly("cn.lunadeer:DominionAPI:4.3")                                    // UltimateClans
    compileOnly("com.intellectualsites.plotsquared:plotsquared-bukkit:7.3.5")     // PlotSquared

    compileOnly("public:Residence:minimize")                                      // Residence, 为维护作者权益已去除所有逻辑代码
    compileOnly("public:CMILib:1.4.7.2:minimize")                                 // CMILib, 为维护作者权益已去除所有逻辑代码
    compileOnly("public:CrashClaim:minimize")                                     // CrashClaim, 为维护作者权益已去除所有逻辑代码
    compileOnly("public:hClaims:minimize")                                        // hClaims, 为维护作者权益已去除所有逻辑代码
    compileOnly("public:KingdomsX:minimize")                                      // KingdomsX, 为维护作者权益已去除所有逻辑代码
    compileOnly("public:NoBuildPlus:1.5.7:minimize")                              // NoBuildPlus (旧版), 为维护作者权益已去除所有逻辑代码
    compileOnly("public:PreciousStones:1.17.1.2:minimize")                        // PreciousStones, 为维护作者权益已去除所有逻辑代码
    compileOnly("public:SuperiorSkyblock2:2023.3:minimize")                       // SuperiorSkyblock2, 为维护作者权益已去除所有逻辑代码
    compileOnly("public:XClaim:minimize")                                         // XClaim, 为维护作者权益已去除所有逻辑代码

    compileOnly("io.github.fabiozumbi12.RedProtect:RedProtect-Core:8.1.1") { exclude(group = "*") }        // RedProtect
    compileOnly("io.github.fabiozumbi12.RedProtect:RedProtect-Spigot:8.1.1") { exclude(group = "*") }      // RedProtect
    compileOnly("ink.ptms:um:1.0.9")                                              // universe-mythic
    compileOnly("net.momirealms:craft-engine-core:26.6")                          // CraftEngine 核心 API
    compileOnly("net.momirealms:craft-engine-bukkit:26.6")                        // CraftEngine Bukkit API
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.2-beta-r3-b")             // ItemsAdder
    compileOnly("net.Indyuce:MMOItems-API:6.9.5-SNAPSHOT")                        // MMOItems
    compileOnly("pers.neige.neigeitems:NeigeItems:1.15.96")                       // NeigeItems
    compileOnly("io.th0rgal:oraxen:1.185.0")                                      // Oraxen
    compileOnly("ink.ptms:Zaphkiel:2.0.14")                                       // Zaphkiel
    compileOnly("public:sx-item:1")                                               // SX-Item
    compileOnly("public:AzureFlow:1")                                             // AzureFlow
    compileOnly("public:pxrpg:1")                                                 // PxRpg
}

// 子模块
taboolib { subproject = true }
