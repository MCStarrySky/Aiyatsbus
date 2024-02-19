import io.izzel.taboolib.gradle.*

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    // 引入 服务端核心
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}

// 子模块
taboolib { subproject = true }