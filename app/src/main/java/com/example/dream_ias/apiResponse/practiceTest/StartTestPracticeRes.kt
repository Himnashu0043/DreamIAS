package com.example.dream_ias.apiResponse.practiceTest

data class StartTestPracticeRes(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var existingMessage: ExistingMessage?
    ) {
        data class ExistingMessage(
            var __v: Int?,
            var _id: String?,
            var answers: List<Any?>?,
            var createdAt: String?,
            var isPrizeWinning: Boolean?,
            var praticeTestId: String?,
            var updatedAt: String?,
            var userId: String?
        )
    }
}