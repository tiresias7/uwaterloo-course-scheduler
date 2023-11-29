package com.example.plugins

import SectionUnit
import com.example.logic.*
import database.common.createDataSource
import database.friends.queryAllFriendRequestsByUID
import database.friends.queryAllFriendsRelationByUID
import database.friends.verifyFriendRelation
import database.sections.queryAllClasses
import database.sections.querySectionsByFacultyId
import database.users.queryExisingUserProfileByUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktor.*
import request.RequestStatus
import request.SignStatus

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/friend") {
            get("/list") {
                val input = call.receive<ID>()

                val friendList = createDataSource().use { queryAllFriendsRelationByUID(input.id, it) }
                call.respond(HttpStatusCode.OK, friendList)
            }

            route("/profile") {
                get("") {
                    val input = call.receive<PairID>()

                    var profile = mutableListOf<SectionUnit>()
                    createDataSource().use {
                        if (verifyFriendRelation(input.senderId, input.receiverId, it)) profile = queryExisingUserProfileByUID(input.receiverId, it)
                        else call.respondText("Friend Relation doesn't exist", ContentType.Text.Plain, HttpStatusCode.BadRequest)
                    }
                    call.respond(HttpStatusCode.OK, profile)
                }
            }
            route("/request") {
                get("") {
                    val input = call.receive<ID>()

                    val requestList = createDataSource().use { queryAllFriendRequestsByUID(input.id, it) }
                    call.respond(HttpStatusCode.OK, requestList)
                }

                post("") {
                    val input = call.receive<PairID>()
                    val requestStatus = sendFriendRequest(input.senderId, input.receiverId)

                    when (requestStatus) {
                        RequestStatus.FRIEND_REQUEST_SELF -> call.respond(HttpStatusCode.BadRequest, "Cannot send friend request to yourself.")
                        RequestStatus.FRIEND_REQUEST_EXIST -> call.respond(HttpStatusCode.Conflict, "Friend request already exists.")
                        RequestStatus.FRIEND_RELATION_EXIST -> call.respond(HttpStatusCode.Conflict, "Friend relation already exists.")
                        RequestStatus.FRIEND_REQUEST_SUCCESS -> call.respond(HttpStatusCode.OK, "Friend request sent successfully.")
                        else -> return@post
                    }
                }

                put("/approval") {
                    val input = call.receive<PairID>()
                    val requestStatus = approveFriendRequest(input.senderId, input.receiverId)

                    when (requestStatus) {
                        RequestStatus.FRIEND_REQUEST_SELF -> call.respond(HttpStatusCode.BadRequest, "Cannot approve own friend request.")
                        RequestStatus.FRIEND_REQUEST_NOT_EXIST -> call.respond(HttpStatusCode.NotFound, "Friend request does not exist.")
                        RequestStatus.FRIEND_RELATION_EXIST -> call.respond(HttpStatusCode.Conflict, "Friend relation already exists.")
                        RequestStatus.FRIEND_REQUEST_SUCCESS -> call.respond(HttpStatusCode.OK, "Friend request approved successfully.")
                        else -> return@put
                    }
                }

                delete("") {
                    val input = call.receive<PairID>()
                    val requestStatus = denyFriendRequest(input.senderId, input.receiverId)

                    when (requestStatus) {
                        RequestStatus.FRIEND_REQUEST_SELF -> call.respond(HttpStatusCode.BadRequest, "Cannot deny own friend request.")
                        RequestStatus.FRIEND_REQUEST_NOT_EXIST -> call.respond(HttpStatusCode.NotFound, "Friend request does not exist.")
                        RequestStatus.FRIEND_REQUEST_SUCCESS -> call.respond(HttpStatusCode.OK, "Friend request denied successfully.")
                        else -> return@delete
                    }
                }
            }

        }

        route("/user") {
            put("/sign-in") {
                val request = call.receive<SignInRequest>()
                val result = signInExistingUsersByEmail(request.email, request.password)

                when (result.first) {
                    SignStatus.SIGN_IN_INVALID -> call.respond(HttpStatusCode.NotFound, "User not found.")
                    SignStatus.SIGN_IN_FAILED -> call.respond(HttpStatusCode.Unauthorized, "Invalid credentials.")
                    SignStatus.SIGN_IN_SUCCESS -> {
                        val cookie = generateUserCookie(result.second)
                        call.respond(HttpStatusCode.OK,  SignResponse(result.second, cookie))
                    }
                    else -> return@put
                }
            }

            post("/sign-up") {
                val request = call.receive<SignUpRequest>()
                val result = signUpNewUsers(request.name, request.passwordHashed, request.email)

                when (result.first) {
                    SignStatus.SIGN_UP_FAILED -> call.respond(HttpStatusCode.Conflict, "User exist.")
                    SignStatus.SIGN_UP_CREATE -> {
                        val cookie = generateUserCookie(result.second)
                        call.respond(HttpStatusCode.Created, SignResponse(result.second, cookie))
                    }
                    else -> return@post
                }
            }
        }

        route("/section") {
            get("courses") {
                val result = createDataSource().use { queryAllClasses(it) }

                call.respond(HttpStatusCode.OK, result)
            }
            get("/faculty/id") {
                val request = call.receive<FacultyIDQueryRequest>()
                val result = createDataSource().use { querySectionsByFacultyId(request.faculty, request.courseID, it) }

                call.respond(HttpStatusCode.OK, result)
            }
        }
    }
}
