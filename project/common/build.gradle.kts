import io.izzel.taboolib.gradle.UNIVERSAL

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}


dependencies {
    // 如果不需要跨平台，可以在此处引入 Bukkit 核心
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}

// 子模块
taboolib { subproject = true }