package logic.signing

import database.common.createDataSource
import database.users.queryUserInfoByEmail
import database.users.verifyPasswordByUIDRaw
import logic.SignStatus

fun signInExistingUsersByEmail(email: String, password: String): Triple<SignStatus, Pair<Int, String>, String> {
    // cookie need to implemented
    var cookie = ""

    createDataSource().use { db ->
        val userInfo = queryUserInfoByEmail(email, db)
        if (userInfo.first == 0) return Triple(SignStatus.SIGN_IN_INVALID, userInfo, cookie)
        if (!verifyPasswordByUIDRaw(userInfo.first, password, db)) {
            return Triple(SignStatus.SIGN_IN_FAILED, userInfo, cookie)
        }
        cookie = "good"
        return Triple(SignStatus.SIGN_IN_SUCCESS, userInfo, cookie)
    }
}