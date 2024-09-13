@file:Suppress("PropertyName", "SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    java
    id("io.izzel.taboolib") version "2.0.13"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "io.izzel.taboolib")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    // TabooLib 配置
    taboolib {
        env {
            install(
                "minecraft-chat",
                "basic-configuration",
                "minecraft-i18n",
                "bukkit-hook",
                "bukkit-util",
                "bukkit-xseries",
                "bukkit-xseries-item",
                "platform-bukkit",
                "platform-bukkit-impl",
                "bukkit-nms",
                "bukkit-nms-stable",
                "bukkit-nms-tag",
                "bukkit-nms-tag-12005",
                "bukkit-nms-tag-legacy",
                "minecraft-kether",
                "bukkit-ui",
                "bukkit-ui-legacy",
                "bukkit-ui-12100",
                "minecraft-effect",
                "minecraft-metrics",
                "script-javascript"
            )
        }
        version {
            taboolib = "6.2.0-beta2"
        }
    }

    // 全局仓库
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("http://mcstarrysky.com:8081/repository/releases/") {
            isAllowInsecureProtocol = true
        }
    }
    // 全局依赖
    dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    }

    // 编译配置
    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_HIGHER
        targetCompatibility = JavaVersion.VERSION_HIGHER
    }
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=all", "-Xextended-compiler-checks")
        }
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}

subprojects
    .filter { it.name != "project" && it.name != "plugin" }
    .forEach { proj ->
        proj.publishing { applyToSub(proj) }
    }

fun PublishingExtension.applyToSub(subProject: Project) {
    repositories {
        maven("http://sacredcraft.cn:8081/repository/releases") {
            isAllowInsecureProtocol = true
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
        mavenLocal()
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = subProject.name
            groupId = "com.mcstarrysky.aiyatsbus"
            version = project.version.toString()
            artifact(subProject.tasks["kotlinSourcesJar"])
            artifact(subProject.tasks["jar"])
            println("> Apply \"$groupId:$artifactId:$version\"")
        }
    }
}