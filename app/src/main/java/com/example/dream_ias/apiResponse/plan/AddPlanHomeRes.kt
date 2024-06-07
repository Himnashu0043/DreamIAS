package com.example.dream_ias.apiResponse.plan

data class AddPlanHomeRes(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
   /* data class Data(
        var __v: Int?,
        var _id: String?,
        var createdAt: String?,
        var isActive: Boolean?,
        var isDeleted: Boolean?,
        var planId: String?,
        var subsType: String?,
        var updatedAt: String?,
        var userId: String?
    )*/
   data class Data(
       var __v: Int?,
       var _id: String?,
       var amount: Int?,
       var createdAt: String?,
       var examId: String?,
       var expiredAt: String?,
       var isActive: Boolean?,
       var isDeleted: Boolean?,
       var isExpired: Boolean?,
       var planId: String?,
       var subsType: String?,
       var updatedAt: String?,
       var userId: String?
   )
}