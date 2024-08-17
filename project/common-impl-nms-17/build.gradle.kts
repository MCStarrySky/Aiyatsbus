dependencies {
    compileOnly(project(":project:common"))
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v11605:11605")
    compileOnly("paper:v12004:12004:core")
}

// 子模块
taboolib { subproject = true }