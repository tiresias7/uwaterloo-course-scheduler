package database.friends

import com.zaxxer.hikari.HikariDataSource

fun createFriendsTableIfNotExists(db: HikariDataSource) {
    db.connection.use { conn ->
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS friends (
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

fun insertNewFriendRelation(id1: Int, id2: Int, db: HikariDataSource) {
    val insertSQL = """
        INSERT IGNORE INTO friends (user1, user2)
        VALUES (LEAST(${id1}, ${id2}), GREATEST(${id1}, ${id2}));
    """.trimIndent()

    db.connection.use { conn ->
        conn.prepareStatement(insertSQL).use { stmt ->
            stmt.execute()
        }
    }
}

fun queryAllFriendsRelationByUID(id: Int, db: HikariDataSource): Set<Int> {
    val querySQL = """
        SELECT *
        FROM friends
        WHERE user1 = ${id} OR user2 = ${id}
    """.trimIndent()
    val ids = mutableSetOf<Int>()

    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.executeQuery().use { result ->
                while (result.next()) {
                    val user1 = result.getInt("user1")
                    val user2 = result.getInt("user2")
                    if (user1 != id) ids.add(user1)
                    if (user2 != id) ids.add(user2)
                }
            }
        }
    }
    return ids
}

fun main() {
    database.common.createDataSource().use {
        createFriendsTableIfNotExists(it)
        insertNewFriendRelation(1, 2, it)
        insertNewFriendRelation(2, 1, it)
        insertNewFriendRelation(3, 4, it)
        insertNewFriendRelation(1, 3, it)
        for (i in queryAllFriendsRelationByUID(1, it)) {
            println(i)
        }
        println("")
        for (i in queryAllFriendsRelationByUID(3, it)) {
            println(i)
        }
        println("")
        for (i in queryAllFriendsRelationByUID(4, it)) {
            println(i)
        }

    }
}

