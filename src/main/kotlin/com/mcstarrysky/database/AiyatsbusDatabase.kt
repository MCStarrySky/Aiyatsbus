package com.mcstarrysky.database

import com.mcstarrysky.database.dao.Expansion
import com.mcstarrysky.database.dao.HistoryExpansion

/**
 * AiyatsbusExpansions
 * com.mcstarrysky.database.AiyatsbusDatabase
 *
 * @author mical
 * @since 2024/2/28 21:45
 */
interface AiyatsbusDatabase {

    fun getExpansion(name: String): Expansion?

    fun addExpansion(expansion: Expansion)

    fun deleteExpansion(expansion: Expansion)

    fun updateExpansion(expansion: Expansion)

    fun getAllExpansions(): List<Expansion>

    fun addOrUpdateHistoryExpansion(expansion: HistoryExpansion)

    fun getHistoryExpansions(): List<HistoryExpansion>

    fun getHistoryExpansionByName(name: String): List<HistoryExpansion>
}