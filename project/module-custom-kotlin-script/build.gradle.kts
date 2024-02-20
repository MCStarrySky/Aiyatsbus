import io.izzel.taboolib.gradle.*

dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    compileOnly("ink.ptms.artifex:artifex:2.0.0")
}

// 子模块
taboolib { subproject = true }