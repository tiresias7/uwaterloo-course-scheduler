package com.example.database.common

import com.example.database.friends.createFriendsTableIfNotExists
import com.example.database.friends.createRequestsTableIfNotExists
import com.example.database.sections.createSectionsTableIfNotExists
import com.example.database.users.createUserProfileTableIfNotExists
import com.example.database.users.createUsersTableIfNotExists
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

object DBUtil {
    val DB_USERNAME = "root"
    val DB_PASSWORD = "PPLegend"
    val DB_URL = "jdbc:mysql://34.130.134.71:3306/courses"

    private val config = HikariConfig().apply {
        jdbcUrl = DB_URL
        username = DB_USERNAME
        password = DB_PASSWORD
    }

    private val dataSource: HikariDataSource = HikariDataSource(config)
    fun getConnection(): Connection {
        return dataSource.connection
    }

    fun tableInitialization() {
        createSectionsTableIfNotExists()
        createUsersTableIfNotExists()
        createFriendsTableIfNotExists()
        createRequestsTableIfNotExists()
        createUserProfileTableIfNotExists()
    }

    fun <T> executeQuery(sql: String, vararg params: Any?, resultHandler: (ResultSet) -> T): T {
        dataSource.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                setParameters(stmt, *params)
                stmt.executeQuery().use { resultSet ->
                    return resultHandler(resultSet)
                }
            }
        }
    }

    fun executeUpdate(sql: String, vararg params: Any?): Int {
        dataSource.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                setParameters(stmt, *params)
                return stmt.executeUpdate()
            }
        }
    }

    private fun setParameters(stmt: PreparedStatement, vararg params: Any?) {
        for (i in params.indices) {
            stmt.setObject(i + 1, params[i])
        }
    }
}

// // Old data cleanup inside table
fun deleteRowsByHours(hours: Int, table: String) {
    val deleteSQL = """
        DELETE FROM $table
        WHERE TIMESTAMPDIFF(HOUR, timestamp, NOW()) >= $hours;
    """.trimIndent()

    DBUtil.executeUpdate(deleteSQL)
}