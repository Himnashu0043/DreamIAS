package com.example.dream_ias.apiResponse

data class FAQListRes(
    var data: ArrayList<Data>?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var addedId: String?,
        var createdAt: String?,
        var description: String?,
        var faqName: String?,
        var isActive: String?,
        var isDeleted: String?,
        var updatedAt: String?
    )
}