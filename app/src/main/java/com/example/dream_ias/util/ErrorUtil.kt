package com.example.dream_ias.util

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.dream_ias.R
import com.example.dream_ias.activity.SplashActivity
import com.example.dream_ias.network.RestClient
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtil {

    fun ResponseBody.getError(context: Context): ErrorModel? {
        val converter = RestClient().get().responseBodyConverter<ErrorModel>(
            ErrorModel::class.java,
            arrayOfNulls<Annotation>(0)
        )
        val errorResponse: ErrorModel? = converter.convert(this)
        if (errorResponse?.message?.equals("Unauthorized access") == true) {
            context.startActivity(Intent(context, SplashActivity::class.java).apply {
                putExtra("fromLogout", true)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            return null
        }
        return errorResponse
    }
    fun handlerGeneralError(view: View?, throwable: Throwable) {
        throwable.printStackTrace()
        if (view == null) return
        when (throwable) {
            is ConnectException -> snackView(view, "Please turn on Internet")
            is SocketTimeoutException -> snackView(view, "Server is not responding. Please try again!")
            is UnknownHostException -> snackView(view, "No Internet Connection.Please check your internet connection!")
            is InternalError -> snackView(view, "Internal Server Error")
            is HttpException -> {
                snackView(view, "Something went wrong")
            }
            else -> {
                snackView(view, "Something went wrong")
            }
        }
    }

    fun snackView(view: View, message: String) {
        try {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }
}


