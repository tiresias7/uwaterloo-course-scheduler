package logic.ktorClient

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FriendClientKtTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun fetchFriendList() {
        runBlocking {
            val temp = signInExistingUsersByEmail("email.com", "password")
            println(temp)
        }
    }

    @Test
    fun fetchFriendRequests() {
    }

    @Test
    fun fetchFriendProfile() {
    }

    @Test
    fun sendFriendRequest() {
    }

    @Test
    fun approveFriendRequest() {
    }

    @Test
    fun denyFriendRequest() {
    }
}