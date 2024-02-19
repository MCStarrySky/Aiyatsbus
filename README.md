# TabooLib SDK (multi-module)

> 多模块 `TabooLib` 项目模板  

## 准备工作

项目结构如下所示

    MyHelloWorld
    ├── plugin                     -- 插件打包模块，用于将子模块合并打包
    │   └── build.gradle.kts
    ├── project                    -- 项目目录
    │   ├── core                   -- 核心模块，公开出去的代码
    │   │   └── build.gradle.kts
    │   └── runtime-bukkit         -- Bukkit 平台启动类，不要把你的业务逻辑写到这里面
    │       └── build.gradle.kts
    ├── build.gradle.kts           -- 全局构建文件
    ├── gradle.properties          -- 全局配置
    ├── settings.gradle.kts        -- 全局配置
    ...

## 构建发行版本

发行版本用于正常使用, 不含 TabooLib 本体。

```
./gradlew clean build
```

## 构建开发版本

开发版本包含 TabooLib 本体, 用于开发者使用, 但不可运行。

```
./gradlew clean taboolibBuildApi -PDeleteCode
```

> 参数 -PDeleteCode 表示移除所有逻辑代码以减少体积。

## 总结

插件该怎么写就在 `core` 里怎么写，`runtime-bukkit` 基本不需要改。