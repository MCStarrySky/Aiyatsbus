import io.izzel.taboolib.gradle.UNIVERSAL

plugins {
    id("io.papermc.paperweight.userdev") version "1.5.10"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":project:common"))

    // 如果不需要跨平台，可以在此处引入 Bukkit 核心
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    taboolibMainTask {
        inJar = File("build/libs/common-impl-modern-${rootProject.version}-dev.jar")
    }
}

// 子模块
taboolib { subproject = true }