package com.example.dream_ias.util

import android.content.Context
import com.example.dream_ias.apiResponse.Data
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefManager private constructor(private val context: Context) {

    private val sharedPref = context.getSharedPreferences("DreamIASPref", Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    var accessToken: String
        get() = sharedPref.getString("accessToken", "") ?: ""
        set(value) {
            editor.putString("accessToken", value).apply()
        }

    var deviceToken: String
        get() = sharedPref.getString("deviceToken", "") ?: ""
        set(value) {
            editor.putString("deviceToken", value).apply()
        }

    var isloggedIn: Boolean
        get() = sharedPref.getBoolean("isloggedIn", false)
        set(value) {
            editor.putBoolean("isloggedIn", value).apply()
        }
    var isTutorial: Boolean
        get() = sharedPref.getBoolean("isTutorial", false)
        set(value) {
            editor.putBoolean("isTutorial", value).apply()
        }
    var isPracticeSubS: Boolean
        get() = sharedPref.getBoolean("isPracticeSubS", false)
        set(value) {
            editor.putBoolean("isPracticeSubS", value).apply()
        }
    var isAllSubS: Boolean
        get() = sharedPref.getBoolean("isAllSubS", false)
        set(value) {
            editor.putBoolean("isAllSubS", value).apply()
        }

    var contestId: String
        get() = sharedPref.getString("contestId", "") ?: ""
        set(value) {
            editor.putString("contestId", value).apply()
        }
    var testId: String
        get() = sharedPref.getString("testId", "") ?: ""
        set(value) {
            editor.putString("testId", value).apply()
        }
    var userId: String
        get() = sharedPref.getString("userId", "") ?: ""
        set(value) {
            editor.putString("userId", value).apply()
        }
    var questionId: String
        get() = sharedPref.getString("questionId", "") ?: ""
        set(value) {
            editor.putString("questionId", value).apply()
        }
    var myContesntSize: String
        get() = sharedPref.getString("myContesntSize", "") ?: ""
        set(value) {
            editor.putString("myContesntSize", value).apply()
        }
    var contesntTime: String
        get() = sharedPref.getString("contesntTime", "0") ?: ""
        set(value) {
            editor.putString("contesntTime", value).apply()
        }
    var liveStreamTime: String
        get() = sharedPref.getString("liveStreamTime", "") ?: ""
        set(value) {
            editor.putString("liveStreamTime", value).apply()
        }
    var liveStreamLink: String
        get() = sharedPref.getString("liveStreamLink", "") ?: ""
        set(value) {
            editor.putString("liveStreamLink", value).apply()
        }
    var contestName: String
        get() = sharedPref.getString("contestName", "") ?: ""
        set(value) {
            editor.putString("contestName", value).apply()
        }
    var pdfSolution: String
        get() = sharedPref.getString("pdfSolution", "") ?: ""
        set(value) {
            editor.putString("pdfSolution", value).apply()
        }
    var practiceTestId: String
        get() = sharedPref.getString("practiceTestId", "") ?: ""
        set(value) {
            editor.putString("practiceTestId", value).apply()
        }

    var linkVideoSolution: String
        get() = sharedPref.getString("linkVideoSolution", "") ?: ""
        set(value) {
            editor.putString("linkVideoSolution", value).apply()
        }
    var wallect_amt: Int
        get() = sharedPref.getInt("wallect_amt", 0)
        set(value) {
            editor.putInt("wallect_amt", value).apply()
        }

    var loginData: Data?
        get() {
            val json = sharedPref.getString("loginData", null)
            val type = object : TypeToken<Data?>() {}.type
            return Gson().fromJson(json, type)
        }
        set(value) {
            val json = Gson().toJson(value)
            editor.putString("loginData", json).apply()
        }

    var userDetail: Data?
        get() {
            val json = sharedPref.getString("userDetail", null)
            val type = object : TypeToken<Data?>() {}.type
            return Gson().fromJson(json, type)
        }
        set(value) {
            val json = Gson().toJson(value)
            editor.putString("userDetail", json).apply()
        }

    fun clearPreferences() {
        editor.clear().apply()
    }

    companion object {
        fun get(context: Context) = PrefManager(context)
    }
}