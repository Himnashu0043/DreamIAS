package com.example.dream_ias.util

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.dream_ias.MainActivity
import com.example.dream_ias.R
import com.example.dream_ias.databinding.ProgressLoaderBinding
import com.google.android.material.snackbar.Snackbar
import com.tooltip.Tooltip
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object CommonUtil {
    const val DELAY_MS: Long = 500
    const val PERIOD_MS: Long = 3000
    /* var noInternetDailog: NoInternetConnectionDailog? = null*/


    fun View.showTooltip(text: String) {
        Tooltip.Builder(this)
            .setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
            .setCornerRadius(R.dimen.dp_4)
            .setTextColor(Color.WHITE)
            .setCancelable(true)
            .setDismissOnClick(true)
            .setText(text)
            .show()
    }

    fun Bundle.printBundle() {
        for (key in keySet()) {
            Log.d("PRINT_BUNDLE", "key: $key value: ${get(key)}")
        }
    }

    fun themeSet(context: Context, window: Window) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(
                context as Activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true
            )
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(
                context as Activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false
            )
            window.statusBarColor = Color.TRANSPARENT
            //window.navigationBarColor  = Color.BLACK
        }


    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win: Window = activity.window
        val winParams: WindowManager.LayoutParams = win.getAttributes()
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.setAttributes(winParams)
    }

    fun indefiniteSnack(context: Context, msg: String): Snackbar {
        val snackbar = Snackbar.make(
            (context as Activity).findViewById(android.R.id.content),
            msg,
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.show()

        return snackbar
    }

    fun showSnackBar(context: Context?, msg: String?, holoGreenDark: Int) {
        var snackbar: Snackbar? = null
        snackbar = if (context is MainActivity) Snackbar.make(
            (context as Activity).findViewById(
                R.id.content
            ), msg!!, Snackbar.LENGTH_LONG
        )
        else Snackbar.make(
            (context as Activity).findViewById(android.R.id.content), msg!!, Snackbar.LENGTH_LONG
        )
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                context, holoGreenDark
            )
        )
        val tv = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
        snackBarView.minimumHeight = 20
        tv.textSize = 14f
        tv.gravity = Gravity.TOP
        tv.setTextColor(ContextCompat.getColor(context, R.color.white))
        snackbar.show()
    }

    fun getAge(date: String?): Int {
        var age = 0
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date1 = formatter.parse(date)
            val now = Calendar.getInstance()
            val dob = Calendar.getInstance()
            dob.time = date1
            require(!dob.after(now)) { "Can't be born in the future" }
            val year1 = now[Calendar.YEAR]
            val year2 = dob[Calendar.YEAR]
            age = year1 - year2
            val month1 = now[Calendar.MONTH]
            val month2 = dob[Calendar.MONTH]
            if (month2 > month1) {
                age--
            } else if (month1 == month2) {
                val day1 = now[Calendar.DAY_OF_MONTH]
                val day2 = dob[Calendar.DAY_OF_MONTH]
                if (day2 > day1) {
                    age--
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        Log.d("TAG", "getAge: AGE=> $age")
        return age
    }

    fun checkGPS(context: Context?): Boolean {
        return try {
            if (context != null) {
                val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                return !(!gps_enabled && !network_enabled)
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    /* fun showHidePassword(context: Context, editText: EditText, iv: ImageView) {
         if (editText.transformationMethod == PasswordTransformationMethod.getInstance()) {
             iv.setImageResource(R.drawable.ic_eye_visible)
             //Show Password
             editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
         } else {
             iv.setImageResource(R.drawable.ic_eye_hide)
             //Hide Password
             editText.transformationMethod = PasswordTransformationMethod.getInstance()
         }

     }*/

    fun blankValidation(str: String?): Boolean {
        return str != null && str.trim().isNotEmpty() && str.trim().length > 9
    }

    fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length in 9..15
        } else false
    }

    fun getProperText(textView: TextView): String? {
        return textView.text.toString().trim { it <= ' ' }
    }

    fun Context.shortToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun startDownload(url: String, con: Context, fileName: String, tittle: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setTitle(tittle)
        request.setMimeType("*/*")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        /*enque manager*/
        val manager = con.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    fun downloadVideo(context: Context, url: String, title: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setTitle(title)
        request.setDescription("Downloading video")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }


    /* fun generateThumbnail(context: Context,videoUrl: String) {
         val outputPath = "/path/to/your/output/thumbnail.jpg"

         // FFmpeg command to generate the thumbnail
         val command = arrayOf("-i", videoUrl, "-ss", "00:00:01.000", "-vframes", "1", outputPath)

         // Execute FFmpeg command
         FFmpeg.getInstance(context).execute(command, object : FFmpegExecuteResponseHandler {
             override fun onSuccess(message: String?) {
                 // Thumbnail generation successful
                 // The thumbnail will be saved at the specified 'outputPath'
             }

             override fun onFailure(message: String?) {
                 // Thumbnail generation failed
                 Log.e("FFmpeg", "Failed to generate thumbnail: $message")
             }

             override fun onProgress(message: String?) {}
             override fun onStart() {}
             override fun onFinish() {}
         })
     }
     fun retrieveVideoFrameFromVideo(videoPath: String?): Bitmap? {
         var bitmap: Bitmap? = null
         var mediaMetadataRetriever: MediaMetadataRetriever? = null
         try {
             mediaMetadataRetriever = MediaMetadataRetriever()
             mediaMetadataRetriever.setDataSource(videoPath, HashMap<String, String>())
             bitmap = mediaMetadataRetriever.frameAtTime
         } catch (e: Exception) {
             e.printStackTrace()
         } finally {
             mediaMetadataRetriever?.release()
         }
         return bitmap
     }*/
    /** check permission for lower version of TIRAMISU*/
    /* fun checkCameraPermission(context: Context): Boolean {
         val camera =
             ContextCompat.checkSelfPermission(context, CAMERA)
         val storage =
             ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE)
         return if (storage != PackageManager.PERMISSION_GRANTED) {
             false
         } else camera == PackageManager.PERMISSION_GRANTED
     }

     @RequiresApi(Build.VERSION_CODES.TIRAMISU)
     fun checkCameraPermission2(context: Context): Boolean {
         val camera =
             ContextCompat.checkSelfPermission(context, CAMERA)
         val image =
             ContextCompat.checkSelfPermission(context, READ_MEDIA_IMAGES)

         return if (image != PackageManager.PERMISSION_GRANTED) {
             false
         } else camera == PackageManager.PERMISSION_GRANTED
     }

     @RequiresApi(Build.VERSION_CODES.TIRAMISU)
     fun checkVideoFilePermission(context: Context): Boolean {
         val camera =
             ContextCompat.checkSelfPermission(context, CAMERA)
         val video =
             ContextCompat.checkSelfPermission(context, READ_MEDIA_VIDEO)
         return if (video != PackageManager.PERMISSION_GRANTED) {
             false
         } else camera == PackageManager.PERMISSION_GRANTED
     }


    fun requestCamFilePermission(activity: Activity) {
         ActivityCompat.requestPermissions(
             activity,
             arrayOf(
                CAMERA,
                READ_EXTERNAL_STORAGE
             ),
             AppConstants.CAMERA_REQUEST_CODE
         )
     }
     @RequiresApi(Build.VERSION_CODES.TIRAMISU)
     fun requestCamFilePermission2(activity: Activity) {
         ActivityCompat.requestPermissions(
             activity,
             arrayOf(
                 CAMERA,
                READ_MEDIA_IMAGES
             ),
             AppConstants.CAMERA_REQUEST_CODE
         )
     }*/

    /* @RequiresApi(Build.VERSION_CODES.TIRAMISU)
     fun requestVideoPermission(activity: Activity) {
         ActivityCompat.requestPermissions(
             activity,
             arrayOf(READ_MEDIA_VIDEO, CAMERA),
             AppConstants.VIDEO_REQ_CODE
         )
     }*/
    fun setLightStatusBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window.decorView.systemUiVisibility = flags
    }

    fun clearLightStatusBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window.decorView.systemUiVisibility = flags
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkCameraFilePermission2(context: Context): Boolean {
        val camera =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val storage =
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
        return if (storage != PackageManager.PERMISSION_GRANTED) {
            false
        } else camera == PackageManager.PERMISSION_GRANTED
    }


    fun requestCamFilePermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            111
        )
    }

    fun requestCamFilePermission2(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            ),
            112
        )
    }

    fun checkCameraPermission(context: Context): Boolean {
        val camera =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val storage =
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        return if (storage != PackageManager.PERMISSION_GRANTED) {
            false
        } else camera == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkCameraPermission2(context: Context): Boolean {
        val camera =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val image =
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)

        return if (image != PackageManager.PERMISSION_GRANTED) {
            false
        } else camera == PackageManager.PERMISSION_GRANTED
    }

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidMobile1(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    /*fun setLanguageForApp(context: Context, languageType: String) {
        Log.e("TAG", "setLanguageForApp${languageType}")
        prefs.put(SessionConstants.LANG,languageType)
        val locale = Locale(languageType)
        Locale.setDefault(locale)
        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        config.locale = locale
        res.updateConfiguration(config, res.displayMetrics)
    }*/

//    fun Context.setAppLocale(language: String): Context {
//        prefs.put(SessionConstants.LANG,language)
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val config = resources.configuration
//        config.setLocale(locale)
//        config.setLayoutDirection(locale)
//        return createConfigurationContext(config)
//    }


    /*fun callLocalization(context: Context,amharichLang: String) {
        val locale = Locale(amharichLang)
        Locale.setDefault(locale)

        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        config.locale = locale
        res.updateConfiguration(config, res.displayMetrics)

    }*/
}


/* fun showNoInternetDialog(activity: Activity?) {
     if (noInternetDailog == null) noInternetDailog = NoInternetConnectionDailog.show(
         activity!!,
         true
     )
     try {
         noInternetDailog!!.setCancelable(false)
         noInternetDailog!!.show()
     } catch (e: Exception) {
         e.printStackTrace()
     }
 }
 fun networkConnectionCheck(context: Context): Boolean {
     val isConnected = isOnline(context)
     if (!isConnected) {
         showNoInternetDialog(context as Activity)
     }
     return isConnected
 }*/

//    fun isOnline(context: Context): Boolean {
//        return try {
//            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//            val wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//            if (mobile_info != null) {
//                if (mobile_info.isConnectedOrConnecting || wifi_info!!.isConnectedOrConnecting) true
//                else false
//            } else {
//                if (wifi_i
//                nfo!!.isConnectedOrConnecting) true
//                else false
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            println("" + e)
//            false
//        }
//    }
object Loaders {
    private var dialog: Dialog? = null

    fun show(context: Context) {
        if (dialog == null) {
            Dialog(context).let {
                val binding = ProgressLoaderBinding.inflate(LayoutInflater.from(context))
                it.requestWindowFeature(Window.FEATURE_NO_TITLE)
                it.window?.setBackgroundDrawableResource(android.R.color.transparent)
                it.setCancelable(false)
                it.setCanceledOnTouchOutside(false)
                it.setContentView(binding.root)
                dialog = it
            }
        }
        dialog?.show()
    }

    fun hide() {
        dialog?.dismiss()
        dialog = null
    }

}

fun String.show(binding: ViewBinding, color: Int) {
    val snackbar = Snackbar.make(binding.root, this, Snackbar.LENGTH_SHORT)
    val snackBarView = snackbar.view
    snackBarView.setBackgroundColor(color)
    snackbar.show()
}

fun setFormatDate(originalDate: String?): String? {
    if (originalDate != "null") {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("dd/MMM/yyyy")
        val date = inputFormat.parse(originalDate)
        val formattedDate = outputFormat.format(date)
        return formattedDate
    }


    return ""

}
fun setFormatDateNew(originalDate: String?): String? {
    if (originalDate != "null") {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = inputFormat.parse(originalDate)
        val formattedDate = outputFormat.format(date)
        return formattedDate
    }


    return ""

}
fun getCurrentDate(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("yyyy-MM-dd")
    val currentDate: String = Dateformat.format(date)

    return currentDate
}
fun isTodayDate(dateString: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val parsedDate = dateFormat.parse(dateString)
    val currentDate = Date()

    val parsedCalendar = Calendar.getInstance()
    parsedCalendar.time = parsedDate

    val currentCalendar = Calendar.getInstance()
    val isSameYear =
        parsedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
    val isSameMonth =
        parsedCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
    val isSameDay =
        parsedCalendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)
    Log.d("showDate", "isSameYear: $isSameYear")
    Log.d("showDate", "isSameMonth: $isSameMonth")
    Log.d("showDate", "isSameDay: $isSameDay")

    return isSameYear && isSameMonth && isSameDay

}
fun isTomorrowdaysDate(dateString: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val parsedDate = dateFormat.parse(dateString)
    val currentDate = Date()

    val parsedCalendar = Calendar.getInstance()
    parsedCalendar.time = parsedDate

    val currentCalendar = Calendar.getInstance()

    val isSameYear =
        parsedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
    val isSameMonth =
        parsedCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
    val isYesterday =
        parsedCalendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH) + 1
    Log.d("showDate", "isSameYear: $isSameYear")
    Log.d("showDate", "isSameMonth: $isSameMonth")
    Log.d("showDate", "isYesterday: $isYesterday")

    return isSameYear && isSameMonth && isYesterday

}
fun getCurrentMillisSinceStartOfDay(): Long {
    val now = Calendar.getInstance()

    // Get the milliseconds for the current time
    val currentMillis = now.timeInMillis

    // Reset hour, minute, second, and millisecond to get the start of the day
    now.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val startOfDayMillis = now.timeInMillis

    // Calculate the milliseconds since the start of the day
    return currentMillis - startOfDayMillis
}

class CustomLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun onRequestChildFocus(
        parent: RecyclerView,
        state: RecyclerView.State,
        child: View,
        focused: View?
    ): Boolean {
        return true
    }
}

fun convertMinutesStringToMillis(minutesString: String): Long {
    // Extract the numeric part of the string
    val minutes = minutesString.filter { it.isDigit() }.toIntOrNull() ?: return -1L
    // Convert minutes to milliseconds
    return minutes * 60_000L
}

fun convertTimeStringToMillis(timeString: String): Long {
    val format = SimpleDateFormat("h:mm a", Locale.US)
    val date = format.parse(timeString) ?: return -1 // Handle parsing error or invalid input

//    val mCal = Calendar.getInstance()
    val calendar = Calendar.getInstance().apply {
        time = date
       /* set(Calendar.YEAR,  mCal.get(Calendar.YEAR))
        set(Calendar.MONTH,  mCal.get(Calendar.MONTH))
        set(Calendar.DAY_OF_MONTH, 1)*/
    }

    // Getting the milliseconds since start of the Unix epoch, then adjust for the offset since this particular epoch start doesn't matter for the calculation.
    val millisSinceStartOfDay = calendar.timeInMillis - Calendar.getInstance().apply {
        set(Calendar.YEAR, 1970)
        set(Calendar.MONTH, Calendar.JANUARY)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    return millisSinceStartOfDay
}

fun convertTimeToMillis(time: String): Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm aa")
    val calendar = Calendar.getInstance()
    calendar.time = sdf.parse("2024-03-19 7:15 PM")!!
    return calendar.timeInMillis
}
fun extractDigitsFromString(inputString: String): String {
    val regex = Regex("[^0-9]") // Regex to match any character that is not a digit
    return inputString.replace(regex, "") // Replace non-digits with an empty string
}
fun convertMinutesToMilliseconds(timeString: String): Long {
    val regex = "(\\d+) min".toRegex() // Regex to extract the numerical value
    val matchResult = regex.find(timeString)
    val minutes = matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
    return (minutes *60 *1000).toLong() // Convert minutes to milliseconds
}
fun getCurrentTimeInMillis(): Long {
    return System.currentTimeMillis()
}
fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("h:mm a")
    return sdf.format(Date())
}
fun extractVideoId(url: String): String? {
    val query = url.split("?").getOrNull(1) ?: return null
    val parameters = query.split("&").associate {
        val (key, value) = it.split("=")
        key to value
    }
    return parameters["v"]
}

