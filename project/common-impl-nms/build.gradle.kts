import io.izzel.taboolib.gradle.UNIVERSAL

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":project:common"))

    // 如果不需要跨平台，可以在此处引入 Bukkit 核心
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11605:11605")
    compileOnly("ink.ptms.core:v11900:11900:mapped")
    compileOnly("ink.ptms.core:v11900:11900:universal")
    compileOnly("ink.ptms.core:v11904:11904:mapped")
    compileOnly("ink.ptms.core:v11904:11904:universal")
    compileOnly("ink.ptms.core:v11802:11802:universal")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v12001:12001:mapped")
    compileOnly("ink.ptms.core:v12001:12001:universal")

    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}

// 子模块
taboolib { subproject = true }