package com.example.dream_ias.aws

import android.content.Context
import android.text.TextUtils
import com.amazonaws.auth.BasicAWSCredentials

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtilityOptions
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client


class AWSUtils(
    private val context: Context?,
    private val filePath: String?,
    val listner: AWSListener?,
) {
    private var image: File? = null
    private var transferUtility: TransferUtility? = null
    private var client: AmazonS3Client? = null

    init {
        TransferNetworkLossHandler.getInstance(context)
//        val credential =
//            CognitoCachingCredentialsProvider(
//                context?.applicationContext,
//                AWSConstants.IDENTITY_POOL_ID,
//                AWSConstants.REGION
//            )
        val credential = BasicAWSCredentials(AWSConstants.ACCESS_KEY, AWSConstants.SECRET_KEY)

        client = AmazonS3Client(credential, Region.getRegion(AWSConstants.REGION))
        val tuOptions = TransferUtilityOptions()
        tuOptions.transferThreadPoolSize = 10
        transferUtility =
            TransferUtility.builder().s3Client(client).context(context?.applicationContext)
                .transferUtilityOptions(tuOptions).build()
        startUploading()
    }

    private fun startUploading() {
        CoroutineScope(Dispatchers.Main).launch {
            coroutineScope {
                with(Dispatchers.IO) {
                    listner?.onAWSLoader(true)
                    if (TextUtils.isEmpty(filePath)) {
                        listner?.onAWSLoader(false)
                        listner?.onAWSError("No such file found!")
                    } else {
                        try {
                            image = File(filePath)
                            val observer = transferUtility?.upload(
                                AWSConstants.BUCKET_NAME,
                                AWSConstants.FOLDER_PATH + image?.name, image
                            )
                            observer?.setTransferListener(
                                AWSCallback(
                                    image,
                                    listner,
                                    folder_path = AWSConstants.FOLDER_PATH + image?.name
                                )
                            )
//                             if (from == "addProfile")
//                            {
//                                val observer = transferUtility?.upload(
//                                    BUCKET_NAME,
//                                    AWSConstants.FOLDER_PATH + image?.name, image
//                                )
//                                observer?.setTransferListener(
//                                    AWSCallback(
//
//                                        image,
//                                        listner,
//                                        folder_path = AWSConstants.FOLDER_PATH + image?.name
//                                    )
//                                )
//                            }

                        } catch (e: Exception) {
                            listner?.onAWSLoader(false)
                            listner?.onAWSError(e.message ?: "")
                        }
                    }
                }
            }
        }
    }
}