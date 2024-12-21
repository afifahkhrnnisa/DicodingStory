package com.fifahkhirnnsa.dicodingstory.ui.upload

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fifahkhirnnsa.dicodingstory.R
import com.fifahkhirnnsa.dicodingstory.data.Result
import com.fifahkhirnnsa.dicodingstory.databinding.ActivityUploadBinding
import com.fifahkhirnnsa.dicodingstory.ui.register.ViewModelFactory
import com.fifahkhirnnsa.dicodingstory.utils.getImageUri
import com.fifahkhirnnsa.dicodingstory.utils.reduceFileImage
import com.fifahkhirnnsa.dicodingstory.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import android.Manifest
import android.content.pm.PackageManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private val viewModel: UploadViewModel by viewModels { ViewModelFactory.getInstance(this) }

    private var currentImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLat: Double? = null
    private var currentLon: Double? = null


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.uploadResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonGallery.setOnClickListener {
            startGallery()
        }

        binding.buttonCamera.setOnClickListener{
            startCamera()
        }

        binding.buttonAdd.setOnClickListener {
            uploadImage()
        }

        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) fetchCurrentLocation() else resetLocation()
        }

        showLoading(false)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }


    private fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    currentLat = it.latitude
                    currentLon = it.longitude
                    showToast("Success added location")
                } ?: showToast("Location not found")
            }
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun resetLocation() {
        currentLat = null
        currentLon = null
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            fetchCurrentLocation()
        } else {
            showToast("Location permission denied")
            binding.switchLocation.isChecked = false
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "Image Path: ${imageFile.path}")

            val description = binding.edAddDescription.text.toString()
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val latRequestBody = currentLat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
            val lonRequestBody = currentLon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            val multipartBody = MultipartBody.Part.createFormData(
                "photo", imageFile.name, requestImageFile
            )

            viewModel.uploadImage(multipartBody, requestBodyDescription, latRequestBody, lonRequestBody)
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
