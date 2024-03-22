repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.2-beta-r3-b")
    compileOnly("com.github.Xiao-MoMi:AntiGriefLib:0.9")
}

// 子模块
taboolib { subproject = true }