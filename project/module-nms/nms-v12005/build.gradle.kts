repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("ink.ptms.core:v12005:12005:mapped")
    compileOnly(project(":project:module-nms:nms-common"))
    compileOnly("com.mojang:brigadier:1.2.9")
}

// 子模块
taboolib { subproject = true }