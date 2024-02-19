import io.izzel.taboolib.gradle.UNIVERSAL

dependencies {
    compileOnly(project(":project:common"))
    compileOnly(project(":project:common-impl-v12004-paper"))
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

// 子模块
taboolib { subproject = true }