package org.ovi.feature.register.view.fragments

import android.app.Activity
import android.net.Uri
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentQuestUploadPicBinding
import org.ovi.feature.profile.model.EditProfileRequest
import org.ovi.feature.profile.viewmodel.FileUploadViewModel
import org.ovi.feature.profile.viewmodel.ProfileViewModel
import org.ovi.feature.register.model.RegisterRequest
import org.ovi.feature.register.view.activity.AdditionalQuestActivity
import org.ovi.feature.register.viewmodel.RegisterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.loadImageIntoViewWithLoading
import org.ovi.util.extensions.showToast
import org.ovi.widget.OviSnackBar


class QuestUploadPicFragment : BaseFragment<FragmentQuestUploadPicBinding>() {

    private val uploadVM: FileUploadViewModel by viewModel()
    private val vm: ProfileViewModel by viewModel { parametersOf(0) }
    private var imageUri: Uri? = null

    private var imageLink: String? = null

    override fun onClick(view: View) {
        when (view) {
            binding.btnNext -> {
                if (imageLink != null)
                    vm.putProfile(EditProfileRequest(image_url = imageLink))
                else
                    showSnackBar("Image not uploaded", OviSnackBar.SnackType.VALIDATION)

            }
            binding.incImageUp.ivUpload -> {

                ImagePicker.with(requireActivity())
                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    )    //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
            binding.tvSkip -> {
                movePager()
            }
        }

    }

    private fun movePager() {
        (requireActivity() as AdditionalQuestActivity).movePager()
    }

    override fun getViewBinding() = FragmentQuestUploadPicBinding.inflate(layoutInflater)

    override fun setupView() {

        with(binding) {
            btnNext.setOnClickListener(onClickListener)
            tvSkip.setOnClickListener(onClickListener)
            incImageUp.ivUpload.setOnClickListener(onClickListener)
        }


    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    imageUri = fileUri
                    uploadVM.uploadFile(fileUri, true)
//                    newVM.uploadFile(fileUri,true)
                }
                ImagePicker.RESULT_ERROR -> {
                    showToast(requireContext(), ImagePicker.getError(data))
                }
                else -> {
                    showToast(requireContext(), "Task Cancelled")
                }
            }
        }


    override fun bindViewModel() {
        bindUpload()
        bindRegister()
    }

    private fun bindRegister() {
        vm.putProfile bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    movePager()
                }
            }
        }
    }

    private fun bindUpload() {
        uploadVM.fileUpload bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                }
                is ResultOf.Progress -> {
                    if (it.loading) binding.progressView.show() else binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    binding.incImageUp.ivUserPic.loadImageIntoViewWithLoading(it.value)
                    imageLink = it.value
                    showSnackBar("Success", OviSnackBar.SnackType.SUCCESS)
                }
            }
        }
    }

}