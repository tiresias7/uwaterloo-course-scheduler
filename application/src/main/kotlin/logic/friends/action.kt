package logic.friends

import database.common.createDataSource
import database.friends.*
import logic.RequestStatus

// id1 is sending friend request to id2
fun sendFriendRequest(id1: Int, id2: Int): RequestStatus {
    if (id1 == id2) return RequestStatus.FRIEND_REQUEST_SELF
    createDataSource().use {
        if (verifyFriendRequest(id1, id2, it)) return RequestStatus.FRIEND_REQUEST_EXIST
        if (verifyFriendRelation(id1, id2, it)) return RequestStatus.FRIEND_RELATION_EXIST
        insertNewFriendRequest(id1, id2, it)
    }
    return RequestStatus.FRIEND_REQUEST_SUCCESS
}

// id1 is approving friend request from id2
fun approveFriendRequest(id1: Int, id2: Int): RequestStatus {
    if (id1 == id2) return RequestStatus.FRIEND_REQUEST_SELF
    createDataSource().use {
        // Doesn't exist friend request from id2 to id1
        if (!verifyFriendRequest(id2, id1, it)) return RequestStatus.FRIEND_REQUEST_NOT_EXIST

        if (verifyFriendRelation(id1, id2, it)) return RequestStatus.FRIEND_RELATION_EXIST
        deleteFriendRequestsByUID(id2, id1, it)
        insertNewFriendRelation(id1, id2, it)
    }
    return RequestStatus.FRIEND_REQUEST_SUCCESS
}

fun denyFriendRequest(id1: Int, id2: Int): RequestStatus {
    if (id1 == id2) return RequestStatus.FRIEND_REQUEST_SELF
    createDataSource().use {
        if (!verifyFriendRequest(id2, id1, it)) return RequestStatus.FRIEND_REQUEST_NOT_EXIST
        deleteFriendRequestsByUID(id2, id1, it)
    }
    return RequestStatus.FRIEND_REQUEST_SUCCESS
}