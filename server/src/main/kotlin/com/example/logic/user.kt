package com.example.logic

import request.SignStatus
import database.users.queryUIDByEmail
import database.common.createDataSource
import database.users.createUser
import database.users.verifyPasswordByUID

fun signUpNewUsers(name: String, passwordHashed: String, email: String): Pair<SignStatus, Int> {
    createDataSource().use { db ->
        val uid = queryUIDByEmail(email, db)
        val status = if (uid == 0) SignStatus.SIGN_UP_CREATE else SignStatus.SIGN_UP_FAILED
        if (status == SignStatus.SIGN_UP_CREATE) {
            createUser(name, passwordHashed, email, db)
        }
        return Pair(status, uid)
    }
}

fun signInExistingUsersByEmail(email: String, password: String): Pair<SignStatus, Int> {
    createDataSource().use { db ->
        val uid = queryUIDByEmail(email, db)
        if (uid == 0) return Pair(SignStatus.SIGN_IN_INVALID, uid)
        if (!verifyPasswordByUID(uid, password, db)) {
            return Pair(SignStatus.SIGN_IN_FAILED, uid)
        }
        return Pair(SignStatus.SIGN_IN_SUCCESS, uid)
    }
}