package com.example.logic

import request.SignStatus
import com.example.database.users.queryUIDNameByEmail
import com.example.database.users.createUser
import com.example.database.users.verifyPasswordByUID

fun signUpNewUsers(name: String, passwordHashed: String, email: String): Pair<SignStatus, Pair<Int, String>> {
    var uidNamePair = queryUIDNameByEmail(email)
    val status = if (uidNamePair.first == 0) SignStatus.SIGN_UP_CREATE else SignStatus.SIGN_UP_FAILED
    if (status == SignStatus.SIGN_UP_CREATE) {
        createUser(name, passwordHashed, email)
        uidNamePair = queryUIDNameByEmail(email)
    }
    return Pair(status, uidNamePair)
}

fun signInExistingUsersByEmail(email: String, password: String): Pair<SignStatus, Pair<Int, String>> {
    val uidNamePair = queryUIDNameByEmail(email)
    if (uidNamePair.first == 0) return Pair(SignStatus.SIGN_IN_INVALID, uidNamePair)
    if (!verifyPasswordByUID(uidNamePair.first, password)) {
        return Pair(SignStatus.SIGN_IN_FAILED, uidNamePair)
    }
    return Pair(SignStatus.SIGN_IN_SUCCESS, uidNamePair)
}