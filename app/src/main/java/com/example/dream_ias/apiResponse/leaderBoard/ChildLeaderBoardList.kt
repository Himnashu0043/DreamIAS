package com.example.dream_ias.apiResponse.leaderBoard

data class ChildLeaderBoardList(
    var data: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var result: ArrayList<Result>?
    ) {
        data class Result(
            var _id: String?,
            var firstName: String?,
            var image: String?,
            var lastName: String?
        )
    }
}