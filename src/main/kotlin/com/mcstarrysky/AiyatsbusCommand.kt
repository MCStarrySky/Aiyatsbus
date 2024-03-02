package com.mcstarrysky

import com.mcstarrysky.database.dao.Expansion
import java.io.File
import java.util.*
import kotlin.system.exitProcess

/**
 * AiyatsbusExpansions
 * com.mcstarrysky.AiyatsbusCommand
 *
 * @author mical
 * @since 2024/3/2 21:10
 */
object AiyatsbusCommand {

    private val thread = Thread {
        Scanner(System.`in`).use {
            while (it.hasNext()) {
                val line = it.nextLine()
                val command = line.split(" ")
                val root = command[0]
                val args = command.subList(1, command.size)
                when (root) {
                    "exit", "quit", "stop", "end", "滚" -> {
                        println("Stopping application...")
                        exitProcess(0)
                    }
                    "update" -> {
                        // update <path> <name> <version> <author> <description>
                        if (args.size < 4) {
                            incorrectArgument(line)
                            continue
                        }
                        val desc = args.subList(4, args.size).joinToString(" ")
                        val path = args[0]
                        val file = File(path)
                        if (!file.exists()) {
                            incorrectArgument(line)
                            continue
                        }
                        val expansion = Expansion().apply {
                            name = args[1]
                            version = args[2]
                            author = args[3]
                            time = System.currentTimeMillis()
                            this.file = path
                            downloadCount = 0
                            description = desc
                        }
                        if (AiyatsbusCommon.DATABASE.getExpansion(args[1])?.version == args[2]) {
                            println("Expansion already exists, updating...")
                            AiyatsbusCommon.DATABASE.updateExpansion(expansion)
                            println("Updating expansion: $expansion")
                            continue
                        }
                        AiyatsbusCommon.DATABASE.addExpansion(expansion)
                        println("Updating expansion: $expansion")
                        continue
                    }
                    else -> {
                        unknownCommand(line)
                        continue
                    }
                }
            }
        }
    }

    fun register() {
        thread.start()
    }

    private fun unknownCommand(str: String) {
        println("Unknown or incomplete command, see below for error")
        println("$str<--[HERE]")
    }

    private fun incorrectArgument(str: String) {
        println("Incorrect argument for command")
        println("$str<--[HERE]")
    }
}