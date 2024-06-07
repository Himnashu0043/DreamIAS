package com.example.dream_ias.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dream_ias.aws.AWSListener
import com.example.dream_ias.aws.AWSUtils
import com.example.dream_ias.databinding.FragmentProfileBinding
import com.example.dream_ias.util.*
import com.example.dream_ias.viewModel.AuthViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(),AWSListener {

    private var profileImage: String = App.app.prefManager.userDetail?.image.toString()
    private val viewModel: AuthViewModel by viewModels()
    private val TAG: String? = ProfileFragment::class.java.name
    private lateinit var progressDialog: MyProgressDialog

    override fun getLayout(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listener()
        observer()
    }

    private fun initView() {

        progressDialog = MyProgressDialog(requireActivity())

        binding.textView50.text = App.app.prefManager.userDetail?.firstName.toString()
        binding.textView501.text = App.app.prefManager.userDetail?.lastName.toString()
        if (!App.app.prefManager.userDetail?.email.isNullOrEmpty()) {
            binding.textView5011.text = App.app.prefManager.userDetail?.email.toString()
            binding.imageView19.visibility = View.VISIBLE
        } else {
            binding.imageView19.visibility = View.GONE
        }

        binding.textView50111.text = App.app.prefManager.userDetail?.phoneNumber.toString()
        if (!App.app.prefManager.userDetail?.image.isNullOrEmpty()) {
            Glide.with(requireContext()).load(App.app.prefManager.userDetail?.image.toString())
                .into(binding.imageView18)
        }


//////////
        binding.editText2.setText(App.app.prefManager.userDetail?.firstName.toString())
        binding.editText3.setText(App.app.prefManager.userDetail?.lastName.toString())
        binding.editText4.setText(App.app.prefManager.userDetail?.email.toString())
        binding.phone.setText(App.app.prefManager.userDetail?.phoneNumber.toString())
        binding.phone.isEnabled = false
        if (!App.app.prefManager.userDetail?.image.isNullOrEmpty()) {
            Glide.with(requireContext()).load(App.app.prefManager.userDetail?.image.toString())
                .into(binding.imageView181)
        }


    }

    private fun listener() {
        binding.textView46.setOnClickListener {
            if (binding.mainProfile.visibility == View.VISIBLE) {
                binding.textView46.text = "Save"
                binding.saveProfile.visibility = View.VISIBLE
                binding.mainProfile.visibility = View.GONE
            } else {
                val token = App.app.prefManager.loginData?.jwtToken.toString()
                val params = HashMap<String, String>()
                params["firstName"] = binding.editText2.text.toString().trim()
                params["lastName"] = binding.editText3.text.toString().trim()
                params["email"] = binding.editText4.text.toString().trim()
                params["phoneNumber"] = binding.phone.text.toString().trim()
                params["image"] = profileImage
                    viewModel.updateUserDetail(token, params)
            }

        }

        binding.imageView181.setOnClickListener {
            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
            showImagePickDialog()

        }

    }

    private fun openGallery() {
        val imagePicker = Intent(Intent.ACTION_GET_CONTENT)
        imagePicker.type = "image/*"
        //pickImageLauncher.launch(imagePicker)

    }

    /*private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri = data?.data
                //            binding.userPic.setImageURI(selectedImageUri)
                Glide.with(requireActivity()).load(selectedImageUri).circleCrop()
                    .into(binding.imageView181)
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

        if (resultCode == RESULT_OK) {
            if (!path.isNullOrEmpty()) {
                Loaders.show(requireContext())
                currentImagePath = Uri.parse(path)
                AWSUtils(
                    requireContext(), path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            if (!path.isNullOrEmpty()) {
                Loaders.show(requireContext())
                currentImagePath = Uri.parse(path)
                AWSUtils(
                    requireContext(), path, this
                )
            }
        }
    }

    private fun observer() {

        viewModel.updateUserDetail.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "observer: Loading")
                }
                is Resource.Success -> {
                    if (it.value?.status == Constants.SUCCESS) {
                        CommonUtil.showSnackBar(
                            requireActivity(),
                            it.value?.message.toString(),
                            android.R.color.holo_green_dark
                        )
                        App.app.prefManager.userDetail = it.value?.data
                        Log.d(TAG, "observer: success")
                        binding.textView46.text = "Edit"
                        binding.saveProfile.visibility = View.GONE
                        binding.mainProfile.visibility = View.VISIBLE

                        binding.textView50.text = App.app.prefManager.userDetail?.firstName.toString()
                        binding.textView501.text = App.app.prefManager.userDetail?.lastName.toString()
                        binding.textView5011.text = App.app.prefManager.userDetail?.email.toString()
                        binding.textView50111.text = App.app.prefManager.userDetail?.phoneNumber.toString()
                        Glide.with(requireContext()).load(App.app.prefManager.userDetail?.image.toString()).into(binding.imageView18)


                    } else {
                        CommonUtil.showSnackBar(
                            requireActivity(),
                            it.value?.message.toString(),
                            android.R.color.holo_red_light
                        )
                    }
                }
                is Resource.Failure -> {
                    ErrorUtil.handlerGeneralError(binding.root, it.throwable)
                }
                else -> {
                    CommonUtil.showSnackBar(
                        requireActivity(),
                        "Something Went Wrong",
                        android.R.color.holo_red_light
                    )
                }
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
        Glide.with(requireActivity()).load(currentImagePath.toString()).into(binding.imageView181)
        profileImage = url ?: App.app.prefManager.userDetail?.image.toString()
    }

    override fun onAWSError(error: String?) {
        Log.e(TAG, "onAWSError: $error")
    }

    override fun onAWSProgress(progress: Int?) {
        Log.d(TAG, "onAWSProgress: $progress")
    }


}