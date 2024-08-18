repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("ink.ptms.core:v12005:12005:mapped")
    compileOnly(project(":project:common"))
    compileOnly(project(":project:common-impl-nms"))
    compileOnly("com.mojang:brigadier:1.2.9")
}

// 子模块
taboolib { subproject = true }