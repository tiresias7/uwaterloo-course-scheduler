package com.example.plugins

import Section
import com.example.logic.*
import com.example.database.friends.queryAllFriendRequestsByUID
import com.example.database.friends.queryAllFriendsRelationByUID
import com.example.database.friends.verifyFriendRelation
import com.example.database.sections.queryAllClasses
import com.example.database.sections.querySectionsByFacultyId
import com.example.database.users.queryExisingUserProfileByUID
import com.example.database.users.resetUserProfileByUID
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
            delete("") {
                val input = call.receive<PairID>()

                val status = deleteFriendRelation(input.senderId, input.receiverId)
                when (status) {
                    RequestStatus.FRIEND_DELETE_SUCCESS -> call.respond(HttpStatusCode.OK, "Friend Relation removed successfully.")
                    else -> call.respond(HttpStatusCode.BadRequest, "Invalid Friend Relation.")
                }
            }

            get("/list") {
                val id = call.request.queryParameters["id"]?.toInt()
                if (id != null) {
                    call.respond(HttpStatusCode.OK, queryAllFriendsRelationByUID(id))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid get parameters")
                }
            }

            route("/profile") {
                get("") {
                    val senderId = call.request.queryParameters["senderId"]?.toInt()
                    val receiverId = call.request.queryParameters["receiverId"]?.toInt()
                    if (senderId != null && receiverId != null) {

                        var profile = mutableListOf<Section>()
                        if (senderId == receiverId || verifyFriendRelation(senderId, receiverId)) {
                            profile = queryExisingUserProfileByUID(receiverId, 1)
                        } else call.respond(HttpStatusCode.BadRequest, profile)

                        call.respond(HttpStatusCode.OK, profile)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid get parameters")
                    }
                }

                put("") {
                    val input = call.receive<ProfileUpdateRequest>()

                    resetUserProfileByUID(input.id, input.profileNumber, input.profile)
                    call.respond(HttpStatusCode.OK, "Put request received.")
                }
            }
            route("/request") {
                get("") {
                    val id = call.request.queryParameters["id"]?.toInt()
                    if (id != null) {
                        call.respond(HttpStatusCode.OK, queryAllFriendRequestsByUID(id))
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid get parameters")
                    }
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
                        val cookie = generateUserCookie(result.second.first)
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
                        val cookie = generateUserCookie(result.second.first)
                        call.respond(HttpStatusCode.Created, SignResponse(result.second, cookie))
                    }
                    else -> return@post
                }
            }
        }

        route("/section") {
            get("/courses") {
                val result = queryAllClasses()

                call.respond(HttpStatusCode.OK, result)
            }
            get("/faculty/id") {
                val faculty = call.request.queryParameters["faculty"]
                val courseID = call.request.queryParameters["courseID"]
                if (!faculty.isNullOrBlank() && !courseID.isNullOrBlank()) {
                    call.respond(HttpStatusCode.OK, querySectionsByFacultyId(faculty, courseID))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid get parameters")
                }
            }
        }
    }
}
