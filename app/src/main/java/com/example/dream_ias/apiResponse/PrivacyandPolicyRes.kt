package com.example.dream_ias.apiResponse

data class PrivacyandPolicyRes(
    var data: ArrayList<Data>?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var createdAt: String?,
        var description: String?,
        var isActive: String?,
        var isDelete: String?,
        var title: String?,
        var type: String?,
        var updatedAt: String?
    )
}