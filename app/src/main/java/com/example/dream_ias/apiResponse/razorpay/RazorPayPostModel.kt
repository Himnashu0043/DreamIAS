package com.example.dream_ias.apiResponse.razorpay

class RazorPayPostModel(
    val id: String,
    val status: String,
    val amount: Int,
    val currency: String,
    val mode: String,
    val purpose: String
)
