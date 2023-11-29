package logic.ktorClient

import SectionUnit
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import request.RequestStatus

suspend fun fetchFriendList(id: Int): List<Pair<Int, String>> {
    return httpClient.get("$baseUrl/friend/list") {
        parameter("id", id)
    }.body()
}

suspend fun fetchFriendRequests(id: Int): List<Pair<Int, String>> {
    return httpClient.get("$baseUrl/friend/request") {
        parameter("id", id)
    }.body()
}

// id1 is trying to query the user profile of id2
suspend fun fetchFriendProfile(senderId: Int, receiverId: Int): List<SectionUnit> {
    return httpClient.get("$baseUrl/friend/profile") {
        parameter("senderId", senderId)
        parameter("receiverId", receiverId)
    }.body()
}

// id1 is sending friend request to id2
suspend fun sendFriendRequest(id1: Int, id2: Int): RequestStatus {
    val respond = httpClient.post("$baseUrl/friend/request") {
        parameter("senderId", id1)
        parameter("receiverId", id2)
    }
    val statusCode = respond.status
    val message: String = respond.body()
    return when (statusCode) {
        HttpStatusCode.OK -> RequestStatus.FRIEND_REQUEST_SUCCESS
        HttpStatusCode.BadRequest -> RequestStatus.FRIEND_REQUEST_SELF
        else -> {
            if ("request" in message) RequestStatus.FRIEND_REQUEST_EXIST else RequestStatus.FRIEND_RELATION_EXIST
        }
    }
}

// id1 is approving friend request from id2
suspend fun approveFriendRequest(id1: Int, id2: Int): RequestStatus {
    val respond = httpClient.put("$baseUrl/friend/request/approval") {
        parameter("senderId", id1)
        parameter("receiverId", id2)
    }
    val statusCode = respond.status
    return when (statusCode) {
        HttpStatusCode.BadRequest -> RequestStatus.FRIEND_REQUEST_SELF
        HttpStatusCode.NotFound -> RequestStatus.FRIEND_REQUEST_NOT_EXIST
        HttpStatusCode.Conflict -> RequestStatus.FRIEND_RELATION_EXIST
        else -> RequestStatus.FRIEND_REQUEST_SUCCESS
    }
}

// id1 is denying friend request from id2
suspend fun denyFriendRequest(id1: Int, id2: Int): RequestStatus {
    val respond = httpClient.put("$baseUrl/friend/request/denial") {
        parameter("senderId", id1)
        parameter("receiverId", id2)
    }
    val statusCode = respond.status
    return when(statusCode) {
        HttpStatusCode.BadRequest -> RequestStatus.FRIEND_REQUEST_SELF
        HttpStatusCode.NotFound -> RequestStatus.FRIEND_REQUEST_NOT_EXIST
        else -> RequestStatus.FRIEND_REQUEST_SUCCESS
    }
}