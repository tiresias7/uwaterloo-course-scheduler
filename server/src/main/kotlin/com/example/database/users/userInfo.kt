package com.example.database.users

import com.example.database.common.DBUtil
import utility.checkPassword
import utility.hashPassword

fun createUsersTableIfNotExists() {
    val createTableSQL = """
        CREATE TABLE IF NOT EXISTS users (
            id INT UNIQUE PRIMARY KEY,
            email VARCHAR(50) unique,
            username VARCHAR(20),
            password VARCHAR(64)
        )
    """.trimIndent()

    DBUtil.executeUpdate(createTableSQL)
}

fun verifyPasswordByUID(id: Int, password: String): Boolean {
    return checkPassword(password, queryHashedPasswordByUID(id))
}

private fun queryHashedPasswordByUID(id: Int): String {
    val querySQL = """
        SELECT password FROM users
        WHERE id = ?
    """.trimIndent()

    return DBUtil.executeQuery(querySQL, id) { result ->
        if (result.next()) result.getString("password")
        else ""
    }
}

fun queryUIDNameByEmail(email: String): Pair<Int, String> {
    val querySQL = """
       SELECT id, username FROM users 
       WHERE email = ?
    """.trimIndent()

    return DBUtil.executeQuery(querySQL, email) { result ->
        if (result.next()) Pair(result.getInt("id"), result.getString("username"))
        else Pair(0, "")
    }
}

fun createUser(userName: String, hashedPassword: String, email: String) {
    val insertSQL = """
        INSERT INTO users (
        SELECT COALESCE(MAX(id) + 1, 1), ?, ?, ?
        FROM users)
        ON DUPLICATE KEY UPDATE id = id
    """.trimIndent()

    DBUtil.executeUpdate(insertSQL, email, userName, hashedPassword)
}

fun createUserRaw(userName: String, password: String, email: String) {
    val hashedPassword = hashPassword(password)
    createUser(userName, hashedPassword, email)
}

fun updatePasswordByUID(id: Int, hashedPassword: String) {
    val updateSQL = """
        UPDATE users
        SET password = ?
        WHERE id  = ?
    """.trimIndent()

    DBUtil.executeUpdate(updateSQL, hashedPassword, id)
}

fun updatePasswordByUIDRaw(id: Int, password: String) {
    val hashedPassword = hashPassword(password)
    updatePasswordByUID(id, hashedPassword)
}

fun main() {
    createUserRaw("newnew", "password", "hello@newnew.com")
    println(checkPassword("password", queryHashedPasswordByUID(queryUIDNameByEmail("hello@newnew.com").first)))
    updatePasswordByUIDRaw(queryUIDNameByEmail("hello@newnew.com").first, "PPlegend")
    println(checkPassword("PPlegend", queryHashedPasswordByUID(queryUIDNameByEmail("hello@newnew.com").first)))
//    createDataSource().use {
//        createUsersTableIfNotExists(it)
//        createUser("xiaoye", "password", "aaa@gmail.com", it)
//        val uid = queryUIDByEmail("aaa@gmail.com", it).first
//        updatePasswordByUIDRaw(uid, "password", it)
//        createUserRaw("eddy", "password", "ccc@gmail.com", it)
//        createUserRaw("alex", "password", "bbb@gmail.com", it)
//        createUserRaw("ryan", "password", "ddd@gmail.com", it)
////        createUserRaw("hello", "password", "eee@gmail.com", it)
//    }
}