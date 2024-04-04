@file:Suppress("PropertyName", "SpellCheckingInspection")

taboolib {
    description {
        name(rootProject.name)
        contributors {
            name("米咔噜")
            name("南外丶仓鼠")
            name("xiaozhangup")
            name("yanshiqwq")
        }
        desc("Aiyatsbus is an powerful enchantment framework for Paper servers.")
        load("STARTUP")
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