repositories {
    maven("https://repo.codemc.org/repository/maven-public/") // BentoBox
}

dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly("world.bentobox:bentobox:2.1.0-SNAPSHOT") // BentoBox
    compileOnly("public:Residence:minimize") // Residence
}

// 子模块
taboolib { subproject = true }