package com.example.dream_ias.apiResponse

data class GetAmountRes(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var amount: Int?,
        var userId: String?
    )
}