@file:Suppress("PropertyName", "SpellCheckingInspection")

// repositories {
//     maven("https://jitpack.io")
// }

dependencies {
    taboo("net.momirealms:AntiGriefLib:dev-5")
}

taboolib {
    description {
        name(rootProject.name)
        contributors {
            name("Mical")
            name("南外丶仓鼠")
            name("xiaozhangup")
            name("yanshiqwq")
        }
        desc("Aiyatsbus is an powerful enchantment framework for Paper servers.")
        load("STARTUP")

        dependencies {
            name("ItemsAdder").optional(true)
        }
    }

    relocate("net.momirealms.antigrieflib", "com.mcstarrysky.aiyatsbus.library.momirealms.antigrieflib")
}

tasks {
    jar {
        // 构件名
        archiveBaseName.set(rootProject.name)
        archiveVersion.set(archiveVersion.get() + "-" + getGitVersionCode())
        // 打包子项目源代码
        rootProject.subprojects.forEach { from(it.sourceSets["main"].output) }
    }
    sourcesJar {
        // 构件名
        archiveBaseName.set(rootProject.name)
        // 打包子项目源代码
        rootProject.subprojects.forEach { from(it.sourceSets["main"].allSource) }
    }
}

fun getGitVersionCode(): String {
    ProcessBuilder("git", "rev-parse", "--short", "HEAD")
        .directory(rootDir)
        .start()
        .inputStream.bufferedReader().use { reader ->
            return reader.readLine()
        }
}