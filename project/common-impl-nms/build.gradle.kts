import io.izzel.taboolib.gradle.UNIVERSAL

dependencies {
    compileOnly(project(":project:common"))
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v11605:11605")
}

// 子模块
taboolib { subproject = true }