repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.2-beta-r3-b")
    compileOnly("org.apache.commons:commons-lang3:3.5")
    compileOnly("com.github.Redempt:Crunch:1.0")
}

// 子模块
taboolib { subproject = true }