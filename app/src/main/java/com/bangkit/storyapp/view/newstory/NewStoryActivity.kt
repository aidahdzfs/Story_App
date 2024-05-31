package com.bangkit.storyapp.view.newstory

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bangkit.storyapp.data.api.response.StoryUploadResponse
import com.bangkit.storyapp.databinding.ActivityNewStoryBinding
import com.bangkit.storyapp.util.getImageUri
import com.bangkit.storyapp.util.reduceFileImage
import com.bangkit.storyapp.util.uriToFile
import com.bangkit.storyapp.view.ViewModelFactory
import com.bangkit.storyapp.view.main.MainActivity
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class NewStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewStoryBinding

    private var currentImageUri: Uri? = null

    private val newStoryViewModel by viewModels<NewStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { uploadStory() }
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d(TAG, "Pilih foto terlebih dahulu")
        }
    }

    private fun showImage(){
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgPreview.setImageURI(it)
        }
    }

    private fun startCamera(){
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadStory(){
        currentImageUri?.let { uri ->
            val img = uriToFile(uri, this).reduceFileImage()
            Log.d("ImageFile", "uploadStory: ${img.path}")
            val desc = binding.edtDesc.text.toString()

            val requestBody = desc.toRequestBody("text/plain".toMediaType())
            val requestImgFile = img.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                img.name,
                requestImgFile
            )

            try {
                newStoryViewModel.isLoading.observe(this@NewStoryActivity){
                    showLoading(it)
                }
                newStoryViewModel.uploadStory(multipartBody, requestBody)
                newStoryViewModel.uploadStory.observe(this@NewStoryActivity){
                    if (!it.error!!){
                        finish()
                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.toString()
                val errorResponse = Gson().fromJson(errorBody, StoryUploadResponse::class.java)
                showToast(errorResponse.message.toString())
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "NewStoryActivity"
    }
}