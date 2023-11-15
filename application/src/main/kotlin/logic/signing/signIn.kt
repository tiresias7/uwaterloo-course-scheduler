package logic.signing

import database.common.createDataSource
import database.users.queryUIDByEmail
import database.users.verifyPasswordByUIDRaw
import logic.SignStatus

fun signInExistingUsersByEmail(email: String, password: String): Triple<SignStatus, Int, String> {
    // cookie need to implemented
    var cookie = ""

    createDataSource().use { db ->
        val uid = queryUIDByEmail(email, db)
        if (uid == 0) return Triple(SignStatus.SIGN_IN_INVALID, uid, cookie)
        if (!verifyPasswordByUIDRaw(uid, password, db)) {
            return Triple(SignStatus.SIGN_IN_FAILED, uid, cookie)
        }
        cookie = "good"
        return Triple(SignStatus.SIGN_IN_SUCCESS, uid, cookie)
    }
}