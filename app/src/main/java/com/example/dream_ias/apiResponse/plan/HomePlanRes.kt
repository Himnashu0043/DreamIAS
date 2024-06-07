package com.example.dream_ias.apiResponse.plan

data class HomePlanRes(
    var data: ArrayList<Data>?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var addedId: String?,
        var configurePrice: ArrayList<ConfigurePrice>?,
        var createdAt: String?,
        var description: String?,
        var examId: String?,
        var examName: String?,
        var icon: String?,
        var isActive: Boolean?,
        var isDeleted: Boolean?,
        var planFeatures: ArrayList<PlanFeature>?,
        var planNo: String?,
        var title: String?,
        var updatedAt: String?
    ) {
        data class ConfigurePrice(
            var _id: String?,
            var duration: String?,
            var popular: String?,
            var price: Int?,
            var validity: Int?
        )

        data class PlanFeature(
            var _id: String?,
            var feature: String?
        )
    }
}