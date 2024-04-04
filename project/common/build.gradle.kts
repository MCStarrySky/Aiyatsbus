repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.2-beta-r3-b")
}

// 子模块
taboolib { subproject = true }