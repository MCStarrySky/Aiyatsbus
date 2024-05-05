dependencies {
    compileOnly("ink.ptms.core:v12005:12005:mapped")
    compileOnly(project(":project:common-impl-nms"))
}

// 子模块
taboolib { subproject = true }