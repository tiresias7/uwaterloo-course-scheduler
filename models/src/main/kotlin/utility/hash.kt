package utility

import org.mindrot.jbcrypt.BCrypt

fun hashPassword(password: String): String {
    val salt = BCrypt.gensalt()
    return BCrypt.hashpw(password, salt)
}

fun checkPassword(plainText: String, hashed: String): Boolean {
    return BCrypt.checkpw(plainText, hashed)
}