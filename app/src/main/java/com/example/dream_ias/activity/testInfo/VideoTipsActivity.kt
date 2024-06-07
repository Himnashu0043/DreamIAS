package com.example.dream_ias.activity.testInfo

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.example.dream_ias.R
import com.example.dream_ias.activity.BaseActivity
import com.example.dream_ias.activity.quiz.QuizMoneyActivity
import com.example.dream_ias.databinding.ActivityVideoTipsBinding
import com.example.dream_ias.util.App
import com.example.dream_ias.util.ExoPlayerUtils.Companion.playVideo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import java.text.DecimalFormat
import java.text.NumberFormat

class VideoTipsActivity : BaseActivity<ActivityVideoTipsBinding>() {
    private var timer: CountDownTimer? = null
    private var timer1: CountDownTimer? = null
    var simpleExoPlayer: ExoPlayer? = null
    private var dialog: Dialog? = null
    private val handle: () -> Unit = {}
    private lateinit var youTubePlayer: YouTubePlayer
    override fun getLayout(): ActivityVideoTipsBinding {
        return ActivityVideoTipsBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvTittle.text =
            if (!App.app.prefManager.contestName.isNullOrEmpty()) App.app.prefManager.contestName else ""

        startTimerQuizStart(App.app.prefManager.contesntTime.toLong())
        println("===============vide0${App.app.prefManager.contesntTime}")

        binding.videoView.visibility = View.VISIBLE
        simpleExoPlayer = binding.videoView.playVideo(
            App.app.prefManager.liveStreamLink,
            Player.REPEAT_MODE_OFF
        ) {
            if (it == Player.STATE_BUFFERING) {
                binding.pro.visibility = View.VISIBLE
            } else {
                binding.pro.visibility = View.GONE
            }
            if (it == Player.STATE_ENDED) {
                handle.invoke()
            }
        }
        /*lifecycle.addObserver(binding.videoView)
        binding.videoView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
//                https://www.youtube.com/watch?v=bcVLqV0HpWs&list=RDGMEMCMFH2exzjBeE_zAHHJOdxgVMWaSwOPCuOdE&index=6
//                val videoId = "S0Q4gqBUs7c"
                println("fsdfhsjksdfsdjnsdvnsvd")
                val videoId = "bcVLqV0HpWs"
                // var videoId =extractVideoId()
                this@VideoTipsActivity.youTubePlayer = youTubePlayer
                youTubePlayer.loadVideo(videoId, 0f)
                youTubePlayer.play()
                youTubePlayer.mute()

            }

        })*/

        /*var liveStream =
            (App.app.prefManager.contesntTime.toLong() - App.app.prefManager.liveStreamTime.toLong())
        startTimerLiveStrem(liveStream)*/

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
       /* timer1?.cancel()
        timer1 = null*/
    }

    private fun startTimerQuizStart(timeValue: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(timeValue, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    App.app.prefManager.contesntTime = millisUntilFinished.toString()
                    /*val progress = ((timeLeft / timeValue.toFloat()) * 100).roundToInt()
                    Log.d("TIMER_QUIZ", "onTick: ${timeLeft / 1000}")*/
                    Log.d("TIMER_QUIZ", "onTick: ${millisUntilFinished / 1000}")
                    val f: NumberFormat = DecimalFormat("00")
                    val hours = millisUntilFinished / 3600000
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    if (hours > 0) {
                        binding.tvTimer.text = binding.root.context.getString(
                            R.string.hour_min_sec,
                            f.format(hours),
                            f.format(min),
                            f.format(sec)
                        )
                    } else {
                        binding.tvTimer.text = binding.root.context.getString(
                            R.string.min_sec,
                            f.format(min),
                            f.format(sec)
                        )
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            override fun onFinish() {
                timer?.cancel()
                timer = null
                Log.d("TIMER_QUIZ", "fiished: ")
                try {
                    Log.d("TIMER_QUIZ", "try: ")
                    binding.tvTimer.text = "00:00"
                    simpleExoPlayer?.pause()
                    startActivity(Intent(this@VideoTipsActivity, QuizMoneyActivity::class.java))
                    finish()
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
        timer?.start()
    }

    private fun startTimerLiveStrem(timeValue1: Long) {
        timer1?.cancel()
        timer1 = object : CountDownTimer(timeValue1, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                   /* App.app.prefManager.liveStreamTime = millisUntilFinished.toString()*/
                    val f: NumberFormat = DecimalFormat("00")
                    val hours = millisUntilFinished / 3600000
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    binding.pro.visibility = View.GONE
                    if (hours > 0) {
                        notifyPopup()
                        var test = dialog?.findViewById<TextView>(R.id.tvLiveStream1)
                        test?.text = binding.root.context.getString(
                            R.string.hour_min_sec,
                            f.format(hours),
                            f.format(min),
                            f.format(sec)
                        )

                    } else {
                        notifyPopup()
                        var test = dialog?.findViewById<TextView>(R.id.tvLiveStream1)
                        test?.text = binding.root.context.getString(
                            R.string.min_sec,
                            f.format(min),
                            f.format(sec)
                        )
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            override fun onFinish() {
                timer1?.cancel()
                timer1 = null
                Log.d("TIMER_QUIZ", "fiished: ")
                try {
                    var test = dialog?.findViewById<TextView>(R.id.tvLiveStream1)
                    test?.text = "00:00"
                    dialog?.dismiss()
                    binding.videoView.visibility = View.VISIBLE
                    simpleExoPlayer = binding.videoView.playVideo(
                        App.app.prefManager.liveStreamLink,
                        Player.REPEAT_MODE_OFF
                    ) {
                        if (it == Player.STATE_BUFFERING) {
                            binding.pro.visibility = View.VISIBLE
                        } else {
                            binding.pro.visibility = View.GONE
                        }
                        if (it == Player.STATE_ENDED) {
                            handle.invoke()
                        }
                    }
                    /*   binding.videoView.visibility = View.VISIBLE
                       lifecycle.addObserver(binding.videoView)
                       binding.videoView.addYouTubePlayerListener(object :
                           AbstractYouTubePlayerListener() {
                           override fun onReady(youTubePlayer: YouTubePlayer) {
   //                https://www.youtube.com/watch?v=bcVLqV0HpWs&list=RDGMEMCMFH2exzjBeE_zAHHJOdxgVMWaSwOPCuOdE&index=6
   //                val videoId = "S0Q4gqBUs7c"
                               println("fsdfhsjksdfsdjnsdvnsvd")
                               val videoId = "bcVLqV0HpWs"
                               // var videoId =extractVideoId()
                               this@VideoTipsActivity.youTubePlayer = youTubePlayer
                               youTubePlayer.loadVideo(videoId, 0f)
                               youTubePlayer.play()
                               youTubePlayer.mute()

                           }

                       })
                       binding.videoView.visibility = View.VISIBLE
                       var videoId =
                           extractVideoId("https://www.youtube.com/watch?v=bcVLqV0HpWs&list=RDGMEMCMFH2exzjBeE_zAHHJOdxgVMWaSwOPCuOdE&index=6")
                       startVideo(videoId.toString())
                       simpleExoPlayer = binding.videoView.playVideo(
                           "https://www.youtube.com/watch?v=bcVLqV0HpWs&list=RDGMEMCMFH2exzjBeE_zAHHJOdxgVMWaSwOPCuOdE&index=6",
                           Player.REPEAT_MODE_OFF
                       ) {
                           if (it == Player.STATE_BUFFERING) {
                               binding.pro.visibility = View.VISIBLE
                           } else {
                               binding.pro.visibility = View.GONE
                           }
                           if (it == Player.STATE_ENDED) {
                               handle.invoke()
                           }
                       }
                       startActivity(Intent(this@VideoTipsActivity, QuizMoneyActivity::class.java))
                       finish()*/
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
        timer1?.start()
    }

    private fun notifyPopup() {
        if (dialog == null) {
            dialog = Dialog(this)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.video_timer_hide_popup)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.show()
            val window = dialog?.window
            if (window != null) {
                window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    /*private fun startVideo(videoId: String) {
        youtubePlayerView = findViewById(R.id.videoView)
        *//*youtubePlayerView.enterFullScreen()
        youtubePlayerView.toggleFullScreen()*//*
        lifecycle.addObserver(youtubePlayerView)
        youtubePlayerView
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // loading the selected video
                // into the YouTube Player
                println("$$$$$${videoId}")
                youTubePlayer.loadVideo(videoId, 0f)
            }


        })
    }*/
}

