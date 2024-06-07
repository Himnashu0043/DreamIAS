package com.example.dream_ias.apiResponse.practiceTest

data class SaveReponcePracticeRes(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var answers: List<Answer?>?,
        var createdAt: String?,
        var isPrizeWinning: Boolean?,
        var noOfAttempts: Int?,
        var praticeTestId: String?,
        var totalIncorrect: Int?,
        var totalScore: Double?,
        var totalcorrectAnswers: Int?,
        var updatedAt: String?,
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
    }
}