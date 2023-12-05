package logic.ktorClient

import Section
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import ktor.PairID
import request.RequestStatus

suspend fun fetchFriendList(id: Int): List<Pair<Int, String>> {
    return httpClient.get("$baseUrl/friend/list") {
        contentType(ContentType.Application.Json)
        parameter("id", id)
    }.body()
}

suspend fun fetchFriendRequests(id: Int): List<Pair<Int, String>> {
    return httpClient.get("$baseUrl/friend/request") {
        contentType(ContentType.Application.Json)
        parameter("id", id)
    }.body()
}

// sender is trying to query the user profile of receiver
suspend fun fetchFriendProfile(senderId: Int, receiverId: Int): List<Section> {
    return httpClient.get("$baseUrl/friend/profile") {
        contentType(ContentType.Application.Json)
        parameter("senderId", senderId)
        parameter("receiverId", receiverId)
    }.body()
}

// sender is sending friend request to receiver
suspend fun sendFriendRequest(senderId: Int, receiverId: Int): RequestStatus {
    val respond = httpClient.post("$baseUrl/friend/request") {
        contentType(ContentType.Application.Json)
        setBody(PairID(senderId, receiverId))
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

// sender is approving friend request from receiver
suspend fun approveFriendRequest(senderId: Int, receiverId: Int): RequestStatus {
    val respond = httpClient.put("$baseUrl/friend/request/approval") {
        contentType(ContentType.Application.Json)
        setBody(PairID(senderId, receiverId))
    }
    val statusCode = respond.status
    return when (statusCode) {
        HttpStatusCode.BadRequest -> RequestStatus.FRIEND_REQUEST_SELF
        HttpStatusCode.NotFound -> RequestStatus.FRIEND_REQUEST_NOT_EXIST
        HttpStatusCode.Conflict -> RequestStatus.FRIEND_RELATION_EXIST
        else -> RequestStatus.FRIEND_REQUEST_SUCCESS
    }
}

// sender is denying friend request from receiver
suspend fun denyFriendRequest(senderId: Int, receiverId: Int): RequestStatus {
    val respond = httpClient.delete("$baseUrl/friend/request") {
        contentType(ContentType.Application.Json)
        setBody(PairID(senderId, receiverId))
    }
    val statusCode = respond.status
    return when(statusCode) {
        HttpStatusCode.BadRequest -> RequestStatus.FRIEND_REQUEST_SELF
        HttpStatusCode.NotFound -> RequestStatus.FRIEND_REQUEST_NOT_EXIST
        else -> RequestStatus.FRIEND_REQUEST_SUCCESS
    }
}

suspend fun deleteFriendRelation(senderId: Int, receiverId: Int): RequestStatus {
    val respond = httpClient.delete("$baseUrl/friend") {
        contentType(ContentType.Application.Json)
        setBody(PairID(senderId, receiverId))
    }
    val statusCode = respond.status
    return when(statusCode) {
        HttpStatusCode.OK -> RequestStatus.FRIEND_DELETE_SUCCESS
        else -> RequestStatus.FRIEND_DELETE_FAILED
    }
}

fun main () {
//    println(deleteFriendRelation(1, 10))
//    println(deleteFriendRelation(10, 1))
//    println(deleteFriendRelation(4, 2))


//    println(LocalTime.now())
//    println(fetchFriendProfile(1,1))
//    println(LocalTime.now())


//    val friendList = fetchFriendList(3)
//    for (pair in friendList) {
//        println(pair.first.toString() + " " + pair.second)
//    }
//
//    println("..........")
//    fetchFriendRequests(7).forEach { println(it.first.toString() + " " + it.second) }
//    println("..........")
//    fetchFriendRequests(11).forEach { println(it.first.toString() + " " + it.second) }
//    println("...........")
//    println(sendFriendRequest(7, 10))
//    println(sendFriendRequest(7, 10))
//    println(sendFriendRequest(7, 7))
//    println(sendFriendRequest(1, 2))
//    println(sendFriendRequest(2, 1))
//    println("...........")
//    println(approveFriendRequest(7,7))
//    println(approveFriendRequest(1,10))
//    println(approveFriendRequest(7, 10))
//    println("...........")
//    println(denyFriendRequest(7, 10))
//    println(denyFriendRequest(10, 7))
//    println(denyFriendRequest(7, 7))
//    println("...........")
//    sendFriendRequest(1, 10)
//    sendFriendRequest(10, 1)
//    approveFriendRequest(10, 1)
}