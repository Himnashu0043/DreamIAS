package com.example.dream_ias.apiResponse.leaderBoard

data class ContestMainLeaderBoardRes(
    var data: ArrayList<Data>?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var answers: ArrayList<Answer>?,
        var contestId: String?,
        var createdAt: String?,
        var isPrizeWinning: Boolean?,
        var noOfAttempts: Int?,
        var prize: Int?,
        var ranking: Int?,
        var testId: String?,
        var totalIncorrect: Int?,
        var totalScore: Double?,
        var totalcorrectAnswers: Int?,
        var updatedAt: String?,
        var userData: UserData?,
        var userId: String?
    ) {
        data class Answer(
            var _id: String?,
            var ans: String?,
            var iscorrect: String?,
            var marks: Int?,
            var queAns: String?,
            var question: String?,
            var questionNo: Int?
        )

        data class UserData(
            var __v: Int?,
            var _id: String?,
            var address: String?,
            var countryFlag: String?,
            var createdAt: String?,
            var deviceToken: String?,
            var deviceType: String?,
            var email: String?,
            var firstName: String?,
            var image: String?,
            var isActive: Boolean?,
            var isDeleted: Boolean?,
            var isSubscribed: Boolean?,
            var jwtToken: String?,
            var kyc: String?,
            var lastName: String?,
            var mobileModel: String?,
            var phoneNumber: Long?,
            var subIsExpired: Boolean?,
            var updatedAt: String?,
            var userNo: String?
        )
    }
}