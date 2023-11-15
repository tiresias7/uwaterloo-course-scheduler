package logic.friends

import data.SectionUnit
import database.common.createDataSource
import database.friends.queryAllFriendRequestsByUID
import database.friends.queryAllFriendsRelationByUID
import database.friends.verifyFriendRelation
import database.users.queryExisingUserProfileByUID

fun fetchFriendList(uid: Int): List<Pair<Int, String>> {
    createDataSource().use {
        return queryAllFriendsRelationByUID(uid, it)
    }
}

fun fetchFriendRequests(uid: Int): List<Pair<Int, String>> {
    createDataSource().use {
        return queryAllFriendRequestsByUID(uid, it)
    }
}

// id1 is trying to query the user profile of id2
fun fetchFriendProfile(id1: Int, id2: Int): List<SectionUnit> {
    var ret = mutableListOf<SectionUnit>()
    createDataSource().use {
        if (!verifyFriendRelation(id1, id2, it)) return ret

        ret = queryExisingUserProfileByUID(id2, it)
    }
    return ret
}
