package com.example.dream_ias.apiResponse.razorpay

class RazorPayValidationRes(
    val account_number: String,
    val fund_account: FundAccount,
    val amount: Int,
    val currency: String,
    val mode: String,
    val purpose: String
)

data class FundAccount(
    val account_type: String,
    val bank_account: BankAccount,
    val contact: Contact
)

data class BankAccount(
    val name: String,
    val ifsc: String,
    val account_number: String
)

data class Contact(
    val name: String,
    val email: String,
    val contact: String
)


