import io.izzel.taboolib.gradle.*

repositories {
    maven("https://jitpack.io")
}

dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.2-beta-r3-b")
}

// 子模块
taboolib { subproject = true }