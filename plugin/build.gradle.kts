@file:Suppress("PropertyName", "SpellCheckingInspection")

taboolib {
    description {
        name(rootProject.name)
        contributors {
            name("Mical")
            name("坏黑")
            name("白熊")
            name("xiaozhangup")
        }

        dependencies {
            name("PlaceholderAPI").optional(true)
            name("ItemsAdder").optional(true)
            name("BentoBox").optional(true)
            name("Residence").optional(true)
            name("QuickShop").optional(true)
            name("QuickShop-Hikari").optional(true)
            name("Citizens").optional(true)
        }

        desc("Aiyatsbus is a powerful enchantment framework for Paper servers.")
        load("STARTUP")
        bukkitApi("1.16")
    }

    relocate("ink.ptms.um", "com.mcstarrysky.aiyatsbus.module.compat.library.um")
    relocate("redempt", "com.mcstarrysky.aiyatsbus.library")
}

dependencies {
    taboo("ink.ptms:um:1.0.9")
    taboo("com.github.Redempt:Crunch:1.0.7")
}

tasks {
    jar {
        // 构件名
        archiveBaseName.set(rootProject.name)
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