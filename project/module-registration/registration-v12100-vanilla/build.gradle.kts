dependencies {
    compileOnly(project(":project:module-registration:registration-v12100-paper"))
    compileOnly("ink.ptms.core:v12101:12101:mapped")
}

// 编译配置
java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_HIGHER
    targetCompatibility = JavaVersion.VERSION_HIGHER
}

// 子模块
taboolib { subproject = true }