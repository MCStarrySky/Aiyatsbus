package com.mcstarrysky.database.library

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource

interface Ormlite {

    val connectionSource: ConnectionSource

    fun <D : Dao<T, *>?, T> createDao(clazz: Class<T>?): D

}