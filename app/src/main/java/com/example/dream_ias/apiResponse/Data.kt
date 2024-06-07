package com.example.dream_ias.apiResponse

data class Data(
    val __v: Int,
    val _id: String,
    val address: String,
    val countryFlag: String,
    val createdAt: String,
    val deviceToken: String,
    val deviceType: String,
    val email: String,
    val firstName: String,
    val image: String,
    val isActive: Boolean,
    val isDeleted: Boolean,
    var jwtToken: String,
    val kyc: String,
    val lastName: String,
    val mobileModel: String,
    val phoneNumber: Long,
    val updatedAt: String,
    val userNo: String,
    val isSubscribed: Boolean,
    val subIsExpired: Boolean,
    val Subscription:Boolean

)