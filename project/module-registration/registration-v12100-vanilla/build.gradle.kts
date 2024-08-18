dependencies {
    compileOnly(project(":project:module-registration:registration-v12100-paper"))
    compileOnly("ink.ptms.core:v12101:12101:mapped")
}

// 子模块
taboolib { subproject = true }