package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private var croppedImageUri: Uri? = null

    private var isCropped = false
    private val tempImageName = "temp_crop.jpg"

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                currentImageUri = uri
                croppedImageUri = null
                isCropped = false
                showImage()
            } else {
                currentImageUri = null
                croppedImageUri = null
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.textTitle) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top + 48
            }
            WindowInsetsCompat.CONSUMED
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.cropClickable.setOnClickListener {
            cropImage()
        }

        binding.analyzeButton.setOnClickListener {
            moveToResult()
        }

        binding.historyButton.setOnClickListener {
            moveToHistory()
        }

        binding.newsButton.setOnClickListener {
            moveToNews()
        }
    }

    private fun startGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun cropImage() {
        if (currentImageUri == null) {
            showToast("Please select an image first")
            return
        }
        currentImageUri?.let { it1 ->
            val tempUri = File(this.cacheDir, tempImageName)
            UCrop.of(it1, tempUri.toUri())
                .withAspectRatio(16.0f, 9.0f)
                .start(this)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            isCropped = true
            croppedImageUri = data?.let { UCrop.getOutput(it) }
            showImage()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showImage() {
        if (!isCropped) {
            currentImageUri?.let {
                binding.previewImageView.setImageURI(currentImageUri)
            }
        } else {
            croppedImageUri?.let {
                binding.previewImageView.setImageURI(croppedImageUri)
            }
        }
    }

    private fun moveToResult() {
        if (currentImageUri == null && croppedImageUri == null) {
            showToast("Please select an image first.")
            return
        }

        val intent = Intent(this, ResultActivity::class.java)

        if (!isCropped) {
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        } else {
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, croppedImageUri.toString())
        }

        startActivity(intent)
    }

    private fun moveToHistory() {
        val intent = Intent(this, ClassificationHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun moveToNews() {
        val intent = Intent(this, CancerNewsActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        this.cacheDir.deleteRecursively()
        super.onDestroy()
    }
}