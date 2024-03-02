package com.mcstarrysky

import com.mcstarrysky.database.AiyatsbusDatabase
import com.mcstarrysky.database.DefaultAiyatsbusDatabaseMysql
import com.mcstarrysky.plugins.*
import com.mcstarrysky.util.unsafeLazy
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File

object AiyatsbusCommon {

    private val account: Pair<String, String> by unsafeLazy {
        val file = File("dao.txt")
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("root\n123456")
        }
        val lines = file.readLines()
        lines[0] to lines[1]
    }

    val DATABASE: AiyatsbusDatabase by unsafeLazy { DefaultAiyatsbusDatabaseMysql(account.first, account.second) }
}

fun main() {
    println("Please enter what will be used as your server port")
    val port = readln().toInt()

    println("Starting com.mcstarrysky.Application...")
    println("Current working directory: ${System.getProperty("user.dir")}")

    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    AiyatsbusCommand.register()
    configureRouting()
}
