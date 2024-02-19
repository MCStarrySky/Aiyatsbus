import io.izzel.taboolib.gradle.*

dependencies {
    compileOnly(project(":project:common"))
    compileOnly(project(":project:common-impl"))
    // 引入 API
    // 引入 服务端核心
    compileOnly("ink.ptms.core:v11903:11903:mapped")
    compileOnly("ink.ptms.core:v11903:11903:universal")
}

// 子模块
taboolib { subproject = true }