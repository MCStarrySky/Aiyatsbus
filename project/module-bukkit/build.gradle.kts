import io.izzel.taboolib.gradle.*

dependencies {
    compileOnly(project(":project:common"))
    compileOnly(project(":project:common-impl"))
}

// 子模块
taboolib { subproject = true }