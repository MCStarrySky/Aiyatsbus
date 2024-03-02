package com.mcstarrysky.database.library

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.DataSourceConnectionSource
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.logger.Level
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class OrmliteMysql(
    private val host: String,
    private val port: Int,
    private val database: String,
    private val user: String,
    private val passwd: String,
    private val ssl: Boolean,
    private val hikariCP: Boolean
) : Ormlite {

    override val connectionSource: ConnectionSource

    init {
        connectionSource = getConnectionSource()
        com.j256.ormlite.logger.Logger.setGlobalLogLevel(Level.WARNING)
    }

    @JvmName("getConnectionSource1")
    private fun getConnectionSource(): ConnectionSource {
        val url = String.format(
            "jdbc:mysql://%s:%s/%s?useSSL=%s&allowPublicKeyRetrieval=true&serverTimezone=UTC&autoReconnect=true",
            host,
            port,
            database,
            ssl
        )
        return if (hikariCP) {
            val config = HikariConfig()
            config.poolName = "MySQLConnectionPool"
            config.minimumIdle = 4
            config.maximumPoolSize = 16
            config.addDataSourceProperty("cachePrepStmts", "true")
            config.addDataSourceProperty("prepStmtCacheSize", "250")
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
            config.jdbcUrl = url
            config.username = user
            config.password = passwd
            DataSourceConnectionSource(HikariDataSource(config), config.jdbcUrl)
        } else {
            JdbcConnectionSource(url, user, passwd)
        }
    }

    override fun <D : Dao<T, *>?, T> createDao(clazz: Class<T>?): D {
        val dao: Dao<T, *> = DaoManager.createDao(connectionSource, clazz)
        if (!dao.isTableExists) {
            TableUtils.createTable(connectionSource, clazz)
        }
        return DaoManager.createDao(connectionSource, clazz)
    }

}