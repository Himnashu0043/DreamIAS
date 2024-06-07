package com.example.dream_ias.apiResponse

data class ExamNameListRes(
    var data: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var result: ArrayList<Result>?
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var addedId: String?,
            var createdAt: String?,
            var examId: String?,
            var examName: String?,
            var icon: String?,
            var isActive: Boolean?,
            var isDeleted: Boolean?,
            var updatedAt: String?
        )
    }
}