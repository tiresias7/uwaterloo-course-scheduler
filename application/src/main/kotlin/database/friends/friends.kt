package database.friends

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

fun queryAllFriendsRelationByUID(id: Int, db: HikariDataSource): List<Pair<Int, String>> {
    val querySQL = """
        SELECT friends.*, users.username
        FROM friends
        JOIN users ON (friends.user1 = users.id OR friends.user2 = users.id)
        WHERE friends.user1 = $id OR friends.user2 = $id
    """.trimIndent()
    val friendData = mutableListOf<Pair<Int, String>>()

    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.executeQuery().use { result ->
                while (result.next()) {
                    val user1 = result.getInt("user1")
                    val user2 = result.getInt("user2")
                    val username = result.getString("username")
                    if (user1 == id) friendData.add(Pair(user2, username))
                    else friendData.add(Pair(user1, username))
                }
            }
        }
    }
    return friendData
}

fun main() {
    database.common.createDataSource().use {
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

