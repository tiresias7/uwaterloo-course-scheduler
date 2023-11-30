package com.example.database.common

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

fun createDataSource(url: String = "jdbc:mysql://34.130.134.71:3306/courses", dbUser:String = "root", dbPass: String = "PPLegend"): HikariDataSource {
    val config = HikariConfig()
    config.jdbcUrl = url
    config.username = dbUser
    config.password = dbPass
    return HikariDataSource(config)
}

// // Old data cleanup inside table
fun deleteRowsByHours(hours: Int, table: String, db: HikariDataSource) {
    db.connection.use { conn ->
        val deleteSQL = """
            DELETE FROM $table
            WHERE TIMESTAMPDIFF(HOUR, timestamp, NOW()) >= ${hours};
        """.trimIndent()

        conn.createStatement().use { stmt ->
            stmt.executeUpdate(deleteSQL)
        }
    }
}