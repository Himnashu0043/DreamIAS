package com.example.dream_ias.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dream_ias.activity.RegistrationActivity
import com.example.dream_ias.aws.AWSListener
import com.example.dream_ias.aws.AWSUtils
import com.example.dream_ias.databinding.FragmentUploadProfileBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.util.CommonUtil.shortToast
import com.example.dream_ias.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UploadPhotoFragment:BaseRegistrationFragment<FragmentUploadProfileBinding>() , AWSListener {

    private lateinit var progressDialog: MyProgressDialog
    private var profileImage: String = ""
    val TAG = UploadPhotoFragment::class.java.name
    private val viewModel: AuthViewModel by viewModels()

    override fun onContinue() {
        val activity = requireActivity() as RegistrationActivity
        activity.params["image"] = profileImage
        moveToNext()


    }

    override fun getLayout(): FragmentUploadProfileBinding {
        return FragmentUploadProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = MyProgressDialog(requireActivity())
        observer()
        binding.tvSkip.setOnClickListener {
            activity.isSkip = true
            //startActivity(Intent(requireContext(), LoginActivity::class.java))
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.signUp(activity.params)
            }
        }
        binding.userPic.setOnClickListener {
            showImagePickDialog()
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (CommonUtil.checkCameraPermission2(requireActivity())) {
                    openGallery()
                } else {
                    CommonUtil.requestCamFilePermission2(requireActivity())
                }
            } else {
                if (CommonUtil.checkCameraPermission(requireActivity())) {
                    openGallery()
                } else {
                    CommonUtil.requestCamFilePermission(requireActivity())
                }
            }*/

        }
    }

    private fun openGallery() {
        val imagePicker = Intent(Intent.ACTION_GET_CONTENT)
        imagePicker.type = "image/*"
        //pickImageLauncher.launch(imagePicker)
    }

    /* private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
         if (result.resultCode == Activity.RESULT_OK) {
             val data: Intent? = result.data
             val selectedImageUri = data?.data
 //            binding.userPic.setImageURI(selectedImageUri)
             Glide.with(requireActivity()).load(selectedImageUri).circleCrop().into(binding.userPic)
             val imgPath = FilePath.getPath(requireActivity(), selectedImageUri!!)
             AWSUtils(
                 requireContext(),
                 imgPath,
                 this
             )

         }
     }*/
    private fun showImagePickDialog() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialog.setMessage("Choose image")
        dialog.setPositiveButton(
            "Gallery"
        ) { _, _ ->
            val intent = Intent(requireContext(), TakeImageWithCrop::class.java)
            intent.putExtra("from", "gallery")
            startActivityForResult(intent, TakeImageWithCrop.GALLERY_REQUEST)
        }
        dialog.setNegativeButton(
            "Camera"
        ) { _, _ ->


            val intent = Intent(requireContext(), TakeImageWithCrop::class.java)
            intent.putExtra("from", "camera")
            startActivityForResult(intent, TakeImageWithCrop.CAMERA_REQUEST)


        }
        dialog.setNeutralButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        dialog.show()
    }

    private var currentImagePath: Uri = Uri.EMPTY
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("filePath")

        if (resultCode == Activity.RESULT_OK) {
            if (!path.isNullOrEmpty()) {
                currentImagePath = Uri.parse(path)
                AWSUtils(
                    requireContext(), path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            if (!path.isNullOrEmpty()) {
                currentImagePath = Uri.parse(path)
                AWSUtils(
                    requireContext(), path, this
                )
            }
        }
    }

    override fun onAWSLoader(isLoader: Boolean) {
        progressDialog.show()
        Log.d(TAG, "onAWSLoader: $isLoader ")
    }

    override fun onAWSSuccess(url: String?) {
        progressDialog.dismiss()
        Log.d(TAG, "onAWSSuccess: $url")
        Glide.with(requireActivity()).load(currentImagePath.toString()).circleCrop().into(binding.userPic)
        profileImage = url ?: ""
    }

    override fun onAWSError(error: String?) {
        progressDialog.dismiss()
        Log.e(TAG, "onAWSError: $error")
    }

    override fun onAWSProgress(progress: Int?) {
        progressDialog.show()
        Log.d(TAG, "onAWSProgress: $progress")
    }

    private fun observer() {
        viewModel.userSignUp.observe(requireActivity()) {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "observer: Loading")
                }
                is Resource.Success -> {
                    if (it.value?.status == Constants.SUCCESS) {
                        App.app.prefManager.loginData = it.value.data
                        requireActivity().shortToast(it.value.message ?: "")
                        moveToNext()
                    } else if (it.value?.status == Constants.STATUS_FAILURE) {
                        requireActivity().shortToast(it.value.message ?: "")
                    } else if (it.value?.status == Constants.STATUS_409) {
                        requireActivity().shortToast(it.value.message ?: "")
                    } else if (it.value?.status == Constants.STATUS_500) {
                        requireActivity().shortToast(it.value.message ?: "")
                    }
                }
                is Resource.Failure -> {
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    requireActivity().shortToast("Something Went Wrong")
                }
            }
        }
    }

}