package database.friends

import com.zaxxer.hikari.HikariDataSource

fun createRequestsTableIfNotExists(db: HikariDataSource) {
    createFriendsCommonTableIfNotExists("requests", db)
}


// Request sent from id1 to id2
fun insertNewFriendRequest(id1: Int, id2: Int, db: HikariDataSource) {
    val insertSQL = """
        INSERT IGNORE INTO requests (user1, user2)
        VALUES (${id1}, ${id2});
    """.trimIndent()

    db.connection.use { conn ->
        conn.prepareStatement(insertSQL).use { stmt ->
            stmt.execute()
        }
    }
}

fun queryAllFriendRequestsByUID(id: Int, db: HikariDataSource): Set<Pair<Int, String>> {
    val querySQL = """
        SELECT DISTINCT requests.user1, users.username
        FROM requests
        JOIN users ON requests.user1 = users.id
        WHERE requests.user2 = $id
    """.trimIndent()
    val friendData = mutableSetOf<Pair<Int, String>>()

    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.executeQuery().use { result ->
                while (result.next()) {
                    val friendId = result.getInt("user1")
                    val friendName = result.getString("name")
                    if (friendId != id) friendData.add(Pair(friendId, friendName))
                }
            }
        }
    }
    return friendData
}


fun deleteFriendRequestsByUID(id1: Int, id2: Int, db: HikariDataSource) {
    val deleteSQl = """
        DELETE FROM requests
        WHERE user1 = $id1 AND  user2 = $id2;
    """.trimIndent()

    db.connection.use { conn ->
        conn.createStatement().use { stmt ->
            stmt.executeUpdate(deleteSQl)
        }
    }
}