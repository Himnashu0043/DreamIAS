package com.example.dream_ias.apiResponse

data class SignUpResponse(
    var data: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var address: String?,
        var countryFlag: String?,
        var createdAt: String?,
        var deviceToken: String?,
        var deviceType: String?,
        var email: String?,
        var firstName: String?,
        var image: String?,
        var isActive: Boolean?,
        var isDeleted: Boolean?,
        var isSubscribed: Boolean?,
        var jwtToken: String?,
        var kyc: String?,
        var lastName: String?,
        var mobileModel: String?,
        var password: String?,
        var phoneNumber: Long?,
        var subIsExpired: Boolean?,
        var updatedAt: String?,
        var userNo: String?
    )
}