package database.users

import com.zaxxer.hikari.HikariDataSource
import org.mindrot.jbcrypt.BCrypt

fun createUsersTableIfNotExists(db: HikariDataSource) {
    db.connection.use { conn ->
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT UNIQUE PRIMARY KEY,
                email VARCHAR(20),
                username VARCHAR(20) UNIQUE,
                password VARCHAR(64)
            )
        """.trimIndent()

        conn.createStatement().use { stmt ->
            stmt.execute(createTableSQL)
        }
    }
}


private fun hashPassword(password: String): String {
    val salt = BCrypt.gensalt()
    return BCrypt.hashpw(password, salt)
}

private fun checkPassword(plainText: String, hashed: String): Boolean {
    return BCrypt.checkpw(plainText, hashed)
}

fun verifyPasswordByUIDRaw(id: Int, password: String, db: HikariDataSource): Boolean {
    return checkPassword(password, queryHashedPasswordByUID(id, db))
}

fun queryHashedPasswordByUID(id: Int, db: HikariDataSource): String {
    val querySQL = """
        SELECT password FROM users
        WHERE id = ?
    """.trimIndent()
    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.setString(1, id.toString())
            stmt.executeQuery().use { result ->
                if (result.next())  return result.getString("password")
                else return ""
            }
        }
    }
}

fun queryUIDByUsername(userName: String, db: HikariDataSource): Int {
    val querySQL = """
       SELECT id FROM users 
       WHERE username = ?
    """.trimIndent()
    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.setString(1, userName)
            stmt.executeQuery().use { result ->
                if (result.next()) return result.getInt("id")
                else return 0
            }
        }
    }
}

fun insertOrUpdatePasswordRaw(userName: String, password: String, db: HikariDataSource, email: String = "") {
    val hashedPassword = hashPassword(password)
    insertOrUpdatePassword(userName, hashedPassword, db, email)
}

fun insertOrUpdatePassword(userName: String, hashedPassword: String, db: HikariDataSource, email: String = "") {
    val insertSQL = """
       INSERT INTO users 
       (email, username, password)
       VALUES (?, ?, ?)
       ON DUPLICATE KEY UPDATE 
           email = VALUES(email),
           password = VALUES(password)
    """.trimIndent()

    db.connection.prepareStatement(insertSQL).use { stmt ->
        stmt.setString(1, email)
        stmt.setString(2, userName)
        stmt.setString(3, hashedPassword)
        stmt.execute()
    }
}

fun main() {
    database.common.createDataSource().use {
        createUsersTableIfNotExists(it)
        insertOrUpdatePasswordRaw("xiaoye", "password", it)
        val uid = queryUIDByUsername("xiaoye", it)
        insertOrUpdatePasswordRaw("alex", "password", it)
        insertOrUpdatePasswordRaw("eddy", "password", it)
        insertOrUpdatePasswordRaw("ryan", "password", it)
        insertOrUpdatePasswordRaw("hello", "password", it)
    }
}