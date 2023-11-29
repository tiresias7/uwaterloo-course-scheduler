package database.friends

import com.zaxxer.hikari.HikariDataSource

fun createFriendsCommonTableIfNotExists(tableName: String, db: HikariDataSource) {
    db.connection.use { conn ->
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS $tableName (
                user1 INT,
                user2 INT,
                PRIMARY KEY (user1, user2),
                FOREIGN KEY (user1) REFERENCES users(id),
                FOREIGN KEY (user2) REFERENCES users(id)
            )
        """.trimIndent()

        conn.createStatement().use{ stmt ->
            stmt.execute(createTableSQL)
        }
    }
}