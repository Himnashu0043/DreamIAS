package com.example.dream_ias.network

import com.example.dream_ias.apiResponse.razorpay.RazorPayPostModel
import com.example.dream_ias.apiResponse.razorpay.RazorPayValidationRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RazorApi {
    @POST("fund_accounts/validations")
    fun razorPayValidation(
        @Header("Authorization") authHeader: String,
        @Body payoutRequest: RazorPayValidationRes
    ): Response<RazorPayPostModel>
}