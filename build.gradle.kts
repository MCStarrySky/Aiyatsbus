@file:Suppress("PropertyName", "SpellCheckingInspection")

import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    java
    id("io.izzel.taboolib") version "2.0.8"
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
            install(CHAT, CONFIGURATION, LANG, BUKKIT_ALL, NMS, NMS_UTIL, KETHER, UI)
            // 开启隔离类加载器
            // enableIsolatedClassloader = true
        }
        version {
            taboolib = "6.1.1-beta5"
            // skipKotlinRelocate = true
        }
    }

    // 全局仓库
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    // 全局依赖
    dependencies {
        compileOnly(kotlin("stdlib"))
    }

    // 编译配置
    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
        maven("https://repo.mcstarrysky.com/releases") {
            credentials {
                username = project.findProperty("starryskyUsername").toString()
                password = project.findProperty("starryskyPassword").toString()
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