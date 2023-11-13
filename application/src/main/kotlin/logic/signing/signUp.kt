package logic.signing

import database.users.queryUIDByUsername
import database.common.createDataSource
import database.users.insertOrUpdatePasswordRaw
import logic.SignStatus
fun signUpNewUsers(name: String, password: String, email: String = ""): Triple<SignStatus, Int, String> {
    // placeholder for cookie now
    var cookie = ""

    createDataSource().use { db ->
        val uid = queryUIDByUsername(name, db)
        val status = if (uid == 0) SignStatus.SIGN_UP_CREATE else SignStatus.SIGN_IN_FAILED
        if (status == SignStatus.SIGN_UP_CREATE) {
            insertOrUpdatePasswordRaw(name, password, db, email)
            cookie = "good"
        }
        return Triple(status, uid, cookie)
    }
}