package com.mcstarrysky.database.dao

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.mcstarrysky.data.ViewedExpansion
import com.mcstarrysky.util.TIME_FORMAT
import com.mcstarrysky.util.unsafeLazy

/**
 * AiyatsbusExpansions
 * com.mcstarrysky.database.dao.HistoryExpansion
 *
 * @author mical
 * @since 2024/2/28 21:37
 */
@DatabaseTable(tableName = "history_expansions")
class HistoryExpansion {

    @DatabaseField(generatedId = true)
    var id: Int = 0

    @DatabaseField
    lateinit var name: String

    @DatabaseField
    lateinit var author: String

    @DatabaseField
    var time: Long = 0

    @DatabaseField
    lateinit var version: String

    @DatabaseField
    lateinit var file: String

    @DatabaseField
    var downloadCount: Long = 0

    @DatabaseField
    var description: String = "这位作者很懒, 还没有写描述~~"

    val formattedTime: String by unsafeLazy { TIME_FORMAT.format(time) }

    fun toViewedExpansion(): ViewedExpansion {
        return ViewedExpansion(name, version, author, downloadCount, time, formattedTime, description, file.split("/").last()).apply {
            historyId = id
        }
    }
}