package com.example.dream_ias.aws

interface AWSListener {
    fun onAWSLoader(isLoader: Boolean)
    fun onAWSSuccess(url: String?)
    fun onAWSError(error: String?)
    fun onAWSProgress(progress : Int?)
}