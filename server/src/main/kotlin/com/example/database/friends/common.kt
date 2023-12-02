package com.example.database.friends

import com.example.database.common.DBUtil

fun createFriendsCommonTableIfNotExists(tableName: String) {
    val createTableSQL = """
        CREATE TABLE IF NOT EXISTS $tableName (
            user1 INT,
            user2 INT,
            PRIMARY KEY (user1, user2),
            FOREIGN KEY (user1) REFERENCES users(id),
            FOREIGN KEY (user2) REFERENCES users(id)
        )
    """.trimIndent()

    DBUtil.executeUpdate(createTableSQL)
}