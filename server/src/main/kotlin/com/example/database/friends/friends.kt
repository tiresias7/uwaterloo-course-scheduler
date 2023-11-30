package com.example.database.friends

import com.example.database.common.createDataSource
import com.zaxxer.hikari.HikariDataSource

fun createFriendsTableIfNotExists(db: HikariDataSource) {
    createFriendsCommonTableIfNotExists("friends", db)
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

fun verifyFriendRelation(id1: Int, id2: Int, db: HikariDataSource): Boolean {
    val querySQL = """
       SELECT * 
       FROM friends
       WHERE user1 = LEAST(${id1}, ${id2}) AND user2 = GREATEST(${id1}, ${id2})
    """.trimIndent()

    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.executeQuery().use { result ->
                return result.next()
            }
        }
    }
}

fun deleteFriendRelationByUID(id1: Int, id2: Int, db: HikariDataSource) {
    val deleteSQl = """
        DELETE FROM friends
        WHERE user1 = LEAST(${id1}, ${id2}) AND user2 = GREATEST(${id1}, ${id2})
    """.trimIndent()

    db.connection.use { conn ->
        conn.createStatement().use { stmt ->
            stmt.executeUpdate(deleteSQl)
        }
    }
}

fun queryAllFriendsRelationByUID(id: Int, db: HikariDataSource): List<Pair<Int, String>> {
    val querySQL = """
        SELECT user2 AS id, username
        FROM friends JOIN users
            ON (friends.user2 = users.id) WHERE user1 = $id
        UNION
        SELECT user1 AS id, username
        FROM friends JOIN users
            ON (friends.user1 = users.id) WHERE user2 = $id
    """.trimIndent()
    val friendData = mutableListOf<Pair<Int, String>>()

    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.executeQuery().use { result ->
                while (result.next()) {
                    val userId = result.getInt("id")
                    val username = result.getString("username")
                    friendData.add(Pair(userId, username))
                }
            }
        }
    }
    return friendData.sortedBy { it.first }
}

fun main() {
    createDataSource().use {
        createFriendsTableIfNotExists(it)
        insertNewFriendRelation(1, 2, it)
        insertNewFriendRelation(2, 1, it)
        insertNewFriendRelation(3, 4, it)
        insertNewFriendRelation(1, 3, it)
        insertNewFriendRelation(2, 4, it)
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
        println(verifyFriendRelation(1, 2, it))
        println(verifyFriendRelation(1, 10, it))
    }
}

