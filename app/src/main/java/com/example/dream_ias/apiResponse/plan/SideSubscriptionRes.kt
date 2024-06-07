package com.example.dream_ias.apiResponse.plan

data class SideSubscriptionRes(
    var data: ArrayList<Data>?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var _id: String?,
        var amount: Int?,
        var createdAt: String?,
        var examId: String?,
        var examName: String?,
        var expiredAt: String?,
        var icon: String?,
        var isActive: Boolean?,
        var isDeleted: Boolean?,
        var isExpired: Boolean?,
        var planId: String?,
        var planNo: String?,
        var subsType: String?,
        var title: String?,
        var updatedAt: String?,
        var userId: String?
    )
}