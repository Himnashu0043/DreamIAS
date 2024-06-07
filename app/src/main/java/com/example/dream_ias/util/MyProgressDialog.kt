package com.example.dream_ias.util

import android.app.ProgressDialog
import android.content.Context

class MyProgressDialog(context: Context) {
    private val progressDialog: ProgressDialog = ProgressDialog(context)

    init {
        progressDialog.setCancelable(true)
        progressDialog.setMessage("Loading...")
    }

    fun show() {
        progressDialog.show()
    }

    fun dismiss() {
        progressDialog.dismiss()
    }

    fun setMessage(message: String) {
        progressDialog.setMessage(message)
    }
}