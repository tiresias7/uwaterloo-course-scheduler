package com.example.database.friends

import com.example.database.common.DBUtil
import com.example.logic.deleteFriendRelation

fun createFriendsTableIfNotExists() {
    createFriendsCommonTableIfNotExists("friends")
}

fun insertNewFriendRelation(id1: Int, id2: Int) {
    val insertSQL = """
        INSERT IGNORE INTO friends (user1, user2)
        VALUES (LEAST(?, ?), GREATEST(?, ?));
    """.trimIndent()

    DBUtil.executeUpdate(insertSQL, id1, id2, id1, id2)
}

fun verifyFriendRelation(id1: Int, id2: Int): Boolean {
    val querySQL = """
       SELECT * 
       FROM friends
       WHERE user1 = LEAST(?, ?) AND user2 = GREATEST(?, ?)
    """.trimIndent()

    return DBUtil.executeQuery(querySQL, id1, id2, id1, id2) { result ->  result.next() }
}

fun deleteFriendRelationByUID(id1: Int, id2: Int) {
    val deleteSQl = """
        DELETE FROM friends
        WHERE user1 = LEAST(?, ?) AND user2 = GREATEST(?, ?)
    """.trimIndent()

    DBUtil.executeUpdate(deleteSQl, id1, id2, id1, id2)
}

fun queryAllFriendsRelationByUID(id: Int): List<Pair<Int, String>> {
    val querySQL = """
        SELECT user2 AS id, username
        FROM friends JOIN users
            ON (friends.user2 = users.id) WHERE user1 = ?
        UNION
        SELECT user1 AS id, username
        FROM friends JOIN users
            ON (friends.user1 = users.id) WHERE user2 = ?
    """.trimIndent()
    val friendData = mutableListOf<Pair<Int, String>>()

    DBUtil.executeQuery(querySQL, id, id) { result ->
        while (result.next()) {
            val userId = result.getInt("id")
            val username = result.getString("username")
            friendData.add(Pair(userId, username))
        }
    }
    return friendData.sortedBy { it.first }
}

fun main() {
    insertNewFriendRelation(1, 10)
    println(verifyFriendRelation(1, 10))
    println(verifyFriendRelation(10, 1))
    println(verifyFriendRelation(5, 1))
    deleteFriendRelation(1, 10)
    for (i in queryAllFriendsRelationByUID(1)) {
        println(i)
    }

//    createDataSource().use {
//        createFriendsTableIfNotExists(it)
//        insertNewFriendRelation(1, 2, it)
//        insertNewFriendRelation(2, 1, it)
//        insertNewFriendRelation(3, 4, it)
//        insertNewFriendRelation(1, 3, it)
//        insertNewFriendRelation(2, 4, it)
//        for (i in queryAllFriendsRelationByUID(1, it)) {
//            println(i)
//        }
//        println("")
//        for (i in queryAllFriendsRelationByUID(3, it)) {
//            println(i)
//        }
//        println("")
//        for (i in queryAllFriendsRelationByUID(4, it)) {
//            println(i)
//        }
//        println(verifyFriendRelation(1, 2, it))
//        println(verifyFriendRelation(1, 10, it))
//    }
}

