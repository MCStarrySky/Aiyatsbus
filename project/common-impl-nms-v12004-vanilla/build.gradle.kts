dependencies {
    compileOnly(project(":project:common"))
    compileOnly(project(":project:common-impl-nms-v12004-paper"))
    compileOnly("ink.ptms.core:v12004:12004:mapped")
}

// 子模块
taboolib { subproject = true }