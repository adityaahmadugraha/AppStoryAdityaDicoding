package com.aditya.appstoryaditya.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.aditya.appstoryaditya.R
import com.aditya.appstoryaditya.databinding.ActivityAddStoryBinding
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.ui.main.MainActivity
import com.aditya.appstoryaditya.util.Constant
import com.aditya.appstoryaditya.util.Constant.setInputError
import com.aditya.appstoryaditya.util.Constant.tokenBearer
import com.aditya.appstoryaditya.util.Resource
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class ActivityAddStory : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel: AddViewModel by viewModels()
    private var user: User? = null
    private var storyFoto: File? = null
    private var fotoStoryPath: String? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            btnCamera.setOnClickListener { startCamera() }
            btnGallery.setOnClickListener { startGallery() }
            btnUpload.setOnClickListener { getUserInput() }
            icBack.setOnClickListener {
                intent = Intent(this@ActivityAddStory, MainActivity::class.java)
                startActivity(intent)
            }
        }

        getUserData()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }


    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(this@ActivityAddStory.packageManager)

        Constant.createCustomTempFile(this@ActivityAddStory).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@ActivityAddStory,
                "com.aditya.appstoryaditya",
                it
            )
            fotoStoryPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }


    private fun getUserInput() {


        binding.apply {
            val deskripsi = etDescription.text.toString()

            if (validateInput(deskripsi, storyFoto)) {

                val filePhotoStory: File = Constant.reduceFileImage(storyFoto as File)
                val requestBody: RequestBody = MultipartBody.Builder()

                    .setType(MultipartBody.FORM)
                    .addFormDataPart("description", deskripsi)
                    .addFormDataPart(
                        "photo",
                        filePhotoStory.name,
                        filePhotoStory.asRequestBody("image/*".toMediaTypeOrNull())
                    ).build()


                insertLaporan(requestBody)
            }
        }
    }

    private fun validateInput(
        deskripsi: String,
        fotoKerusakan: File?
    ): Boolean {
        binding.apply {
            if (deskripsi.isEmpty()) {
                return ilDescription.setInputError(getString(R.string.tidak_boleh_kosong))
            }

            if (fotoKerusakan == null) {
                Toast.makeText(
                    this@ActivityAddStory,
                    getString(R.string.pick_photo_first),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }

    private fun insertLaporan(requestBody: RequestBody) {
        viewModel.inputLaporan(
            user?.tokenBearer.toString(), requestBody
        ).observe(this@ActivityAddStory) { result ->
            binding.apply {
                when (result) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        progressBar.visibility = View.GONE

                        Intent(this@ActivityAddStory, MainActivity::class.java).apply {
                            startActivity(this)
                        }

                    }

                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@ActivityAddStory, result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }


    private fun getUserData() {
        viewModel.getUser().observe(this) {
            user = it
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val photoShooted = File(fotoStoryPath.toString())
            val rotatedBitmap = Constant.getRotatedBitmap(photoShooted)
            lifecycleScope.launch(Dispatchers.IO) {
                val uri = rotatedBitmap?.let { it1 ->
                    Constant.bitmapToFile(
                        it1,
                        this@ActivityAddStory
                    )
                }
                storyFoto = File(uri?.path.toString())

            }
            Glide.with(this@ActivityAddStory)
                .load(rotatedBitmap)
                .into(binding.imgStory)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                storyFoto = Constant.uriToFile(uri, this@ActivityAddStory)
                binding.imgStory.setImageURI(uri)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is TextInputEditText || v is AutoCompleteTextView) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}