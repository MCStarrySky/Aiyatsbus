package com.mcstarrysky.database

import com.j256.ormlite.dao.Dao
import com.mcstarrysky.database.dao.Expansion
import com.mcstarrysky.database.dao.HistoryExpansion
import com.mcstarrysky.database.library.Ormlite
import com.mcstarrysky.util.unsafeLazy

/**
 * AiyatsbusExpansions
 * com.mcstarrysky.database.DefaultAiyatsbusDatabase
 *
 * @author mical
 * @since 2024/2/28 21:48
 */
abstract class DefaultAiyatsbusDatabase(ormlite: Ormlite) : AiyatsbusDatabase {

    private val expansionTable: Dao<Expansion, Int> by unsafeLazy { ormlite.createDao(Expansion::class.java) }
    private val historyExpansionTable: Dao<HistoryExpansion, Int> by unsafeLazy { ormlite.createDao(HistoryExpansion::class.java) }

    override fun getExpansion(name: String): Expansion? {
        return expansionTable.queryBuilder()
            .where().eq("name", name)
            .queryForFirst()
    }

    override fun addExpansion(expansion: Expansion) {
        getExpansion(expansion.name)?.let {
            HistoryExpansion().apply {
                name = it.name
                author = it.author
                time = it.time
                version = it.version
                file = it.file
                downloadCount = it.downloadCount
                description = it.description
            }.let { addOrUpdateHistoryExpansion(it) }
            updateExpansion(expansion)
        } ?: expansionTable.create(expansion)
    }

    override fun deleteExpansion(expansion: Expansion) {
        expansionTable.delete(expansion)
    }

    override fun updateExpansion(expansion: Expansion) {
        expansionTable.update(expansion)
    }

    override fun getAllExpansions(): List<Expansion> {
        return expansionTable.queryForAll()
    }

    override fun addOrUpdateHistoryExpansion(expansion: HistoryExpansion) {
        historyExpansionTable.createOrUpdate(expansion)
    }

    override fun getHistoryExpansions(): List<HistoryExpansion> {
        return historyExpansionTable.queryForAll()
    }

    override fun getHistoryExpansionByName(name: String): List<HistoryExpansion> {
        return historyExpansionTable.queryBuilder()
            .where().eq("name", name)
            .query()
    }
}