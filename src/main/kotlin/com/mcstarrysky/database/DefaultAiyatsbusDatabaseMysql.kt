package com.mcstarrysky.database

import com.mcstarrysky.database.library.OrmliteMysql

/**
 * AiyatsbusExpansions
 * com.mcstarrysky.database.DefaultAiyatsbusDatabaseMysql
 *
 * @author mical
 * @since 2024/2/28 21:59
 */
class DefaultAiyatsbusDatabaseMysql(
    user: String,
    passwd: String
) : DefaultAiyatsbusDatabase(
    OrmliteMysql(
        host = "localhost",
        port = 3306,
        database = "ktor",
        user = user,
        passwd = passwd,
        ssl = false,
        hikariCP = true
    )
)