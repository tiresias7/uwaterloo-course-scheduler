package ktor

import kotlinx.serialization.Serializable

@Serializable
data class ID(val id: Int)

@Serializable
data class PairID(val senderId: Int, val receiverId: Int)

