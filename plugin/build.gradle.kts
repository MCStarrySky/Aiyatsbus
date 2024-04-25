@file:Suppress("PropertyName", "SpellCheckingInspection")

taboolib {
    description {
        name(rootProject.name)
        contributors {
            name("米咔噜")
            name("坏黑")
            name("南外丶仓鼠")
            name("xiaozhangup")
            name("yanshiqwq")
            name("Itz_Dr_Li")
        }

        dependencies {
            name("PlaceholderAPI").optional(true)
            name("ItemsAdder").optional(true)
            name("BentoBox").optional(true)
            name("Residence").optional(true)
        }

        desc("Aiyatsbus is a powerful enchantment framework for Paper servers.")
        load("STARTUP")
        bukkitApi("1.16")
    }
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