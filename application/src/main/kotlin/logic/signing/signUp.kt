package logic.signing

import database.common.createDataSource
import database.users.createUserRaw
import database.users.queryUserInfoByEmail
import logic.SignStatus
fun signUpNewUsers(name: String, password: String, email: String): Triple<SignStatus, Pair<Int, String>, String> {
    // placeholder for cookie now
    var cookie = ""

    createDataSource().use { db ->
        val userInfo = queryUserInfoByEmail(email, db)
        val status = if (userInfo.first == 0) SignStatus.SIGN_UP_CREATE else SignStatus.SIGN_UP_FAILED
        if (status == SignStatus.SIGN_UP_CREATE) {
            createUserRaw(name, password, email, db)
            cookie = "good"
        }
        return Triple(status, userInfo, cookie)
    }
}