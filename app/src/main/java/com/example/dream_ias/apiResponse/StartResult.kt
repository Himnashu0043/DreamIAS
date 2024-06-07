package com.example.dream_ias.apiResponse

data class StartResult(
    var `data`: Data?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var answers: List<Any?>?,
        var contestId: String?,
        var createdAt: String?,
        var testId: String?,
        var updatedAt: String?,
        var userId: String?
    )
}