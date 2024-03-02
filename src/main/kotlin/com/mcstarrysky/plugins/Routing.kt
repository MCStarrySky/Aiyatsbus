package com.mcstarrysky.plugins

import com.mcstarrysky.AiyatsbusCommon
import com.mcstarrysky.data.ViewedExpansion
import com.mcstarrysky.util.GSON
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    routing {
        get("/") {
            val expansions = AiyatsbusCommon.DATABASE.getAllExpansions()
                .map { it.toViewedExpansion() }
            call.respondText(GSON.toJson(expansions))
        }
        get("/history") {
            call.respondText(GSON.toJson(AiyatsbusCommon.DATABASE.getHistoryExpansions().map { it.toViewedExpansion() }.sortedBy { it.time }.asReversed()))
        }
        get("/history/{plugin}") {
            val plugin = call.parameters["plugin"]
            if (plugin == null) {
                call.respondText("[]")
                return@get
            }
            val list = mutableListOf<ViewedExpansion>()
            AiyatsbusCommon.DATABASE.getExpansion(plugin)?.let { list += it.toViewedExpansion() }
            list += AiyatsbusCommon.DATABASE.getHistoryExpansionByName(plugin).map { it.toViewedExpansion() }
            call.respondText(GSON.toJson(list.sortedBy { it.time }.asReversed()))
        }
        get("/download/{plugin}/{historyId}") {
            val plugin = call.parameters["plugin"] ?: return@get
            val historyId = call.parameters["historyId"]?.toIntOrNull() ?: -1
            val file: String
            if (historyId == -1) {
                val ex = AiyatsbusCommon.DATABASE.getExpansion(plugin) ?: return@get
                ex.downloadCount += 1
                AiyatsbusCommon.DATABASE.updateExpansion(ex)
                file = ex.file
            } else {
                val ex = AiyatsbusCommon.DATABASE.getHistoryExpansionByName(plugin).firstOrNull { it.id == historyId } ?: return@get
                ex.downloadCount += 1
                AiyatsbusCommon.DATABASE.addOrUpdateHistoryExpansion(ex)
                file = ex.file
            }
            call.response.header(
                HttpHeaders.ContentDisposition, ContentDisposition.Attachment.withParameter(
                    ContentDisposition.Parameters.FileName,
                    file.split("/").last()
                ).toString()
            )
            call.respondFile(File(file))
        }
    }
}
