package logic.signing

import database.users.queryUIDByEmail
import database.common.createDataSource
import database.users.createUser
import logic.SignStatus
fun signUpNewUsers(name: String, password: String, email: String): Triple<SignStatus, Int, String> {
    // placeholder for cookie now
    var cookie = ""

    createDataSource().use { db ->
        val uid = queryUIDByEmail(email, db)
        val status = if (uid == 0) SignStatus.SIGN_UP_CREATE else SignStatus.SIGN_UP_FAILED
        if (status == SignStatus.SIGN_UP_CREATE) {
            createUser(name, password, email, db)
            cookie = "good"
        }
        return Triple(status, uid, cookie)
    }
}