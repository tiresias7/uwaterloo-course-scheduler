package com.example.database.friends

import com.example.database.common.DBUtil

fun createRequestsTableIfNotExists() {
    createFriendsCommonTableIfNotExists("requests")
}

// Request sent from id1 to id2
fun insertNewFriendRequest(id1: Int, id2: Int) {
    val insertSQL = """
        INSERT IGNORE INTO requests (user1, user2)
        VALUES (?, ?);
    """.trimIndent()

    DBUtil.executeUpdate(insertSQL, id1, id2)
}

fun verifyFriendRequest(id1: Int, id2: Int): Boolean {
    val querySQL = """
       SELECT * 
       FROM requests
       WHERE user1 = ? AND user2 = ?
    """.trimIndent()

    return DBUtil.executeQuery(querySQL, id1, id2) { result -> result.next() }
}

fun queryAllFriendRequestsByUID(id: Int): List<Pair<Int, String>> {
    val querySQL = """
        SELECT DISTINCT requests.user1, users.username
        FROM requests
        JOIN users ON requests.user1 = users.id
        WHERE requests.user2 = ?
    """.trimIndent()
    val friendData = mutableListOf<Pair<Int, String>>()

    DBUtil.executeQuery(querySQL, id) { result ->
        while (result.next()) {
            val friendId = result.getInt("user1")
            val friendName = result.getString("username")
            if (friendId != id) friendData.add(Pair(friendId, friendName))
        }
    }
    return friendData
}


fun deleteFriendRequestsByUID(id1: Int, id2: Int) {
    val deleteSQl = """
        DELETE FROM requests
        WHERE user1 = ? AND  user2 = ?
    """.trimIndent()

    DBUtil.executeUpdate(deleteSQl, id1, id2)
}

fun main() {
    insertNewFriendRequest(10, 4)
    println(verifyFriendRequest(10, 4))
    println(verifyFriendRequest(10, 5))
    queryAllFriendRequestsByUID(13).forEach {
        println(it.first.toString() + it.second)
    }
    queryAllFriendRequestsByUID(1).forEach {
        println(it.first.toString() + it.second)
    }
    deleteFriendRequestsByUID(10, 4)
}