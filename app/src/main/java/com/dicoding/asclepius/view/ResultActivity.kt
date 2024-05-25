package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dicoding.asclepius.data.local.database.ClassificationHistory
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.helper.NumberUtil.toReadablePercentage
import com.dicoding.asclepius.view.model.ResultActivityModel
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.util.Date

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private lateinit var savedImageDir: String
    private var savedImageNameFormat = "%s.jpg"
    private var savedImageUri: Uri? = null

    private val resultActivityModel: ResultActivityModel by viewModels {
        ResultActivityModel.Factory
    }

    private var currentImageUri: Uri? = null
    private var currentImageLabel: String? = null
    private var currentImageScore: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        currentImageLabel = intent.getStringExtra(EXTRA_IMAGE_LABEL)
        currentImageScore = intent.getFloatExtra(EXTRA_IMAGE_SCORE, -1f)

        imageClassifierHelper = ImageClassifierHelper(
            ctx = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        showToast(error)
                    }
                }

                override fun onResults(
                    results: MutableList<Classifications>?,
                    inferenceTime: Long
                ) {
                    results?.let { it ->
                        if (it.isNotEmpty() && it.first().categories.isNotEmpty()) {
                            currentImageScore = it.first().categories.first().score
                            currentImageLabel = it.first().categories.first().label

                            runOnUiThread {
                                showToast("Image has been successfully classified with TFLite")
                                showAnalysis()
                            }
                        } else {
                            showToast("An unknown TFLite error occurred")
                        }
                    }
                }
            }
        )
        analyzeImage()

        binding.saveButton.setOnClickListener {
            saveAnalysisResult()
        }

        binding.topAppBar.setNavigationOnClickListener {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun analyzeImage() {
        if (currentImageLabel != null && currentImageScore != -1f) {
            showAnalysis()
            return
        }
        currentImageUri?.let { imageClassifierHelper.classifyStaticImage(it) }
    }

    private fun showAnalysis() {
        binding.resultImage.setImageURI(currentImageUri)
        binding.resultText.text = String.format(
            "%s %s",
            currentImageScore!!.toReadablePercentage(), currentImageLabel
        )
    }

    private fun saveAnalysisResult() {
        savedImageDir = this.externalMediaDirs[0].toString() + "/classification_history"
        File(savedImageDir).mkdirs()

        val inputStream = currentImageUri?.let { contentResolver.openInputStream(it) }
        val outputFile = File(savedImageDir, String.format(savedImageNameFormat, Date().time))

        inputStream?.copyTo(outputFile.outputStream())
        inputStream?.close()

        savedImageUri = outputFile.toUri()

        resultActivityModel.insert(
            ClassificationHistory(
                savedImageUri.toString(),
                currentImageLabel!!,
                currentImageScore!!
            )
        )

        showToast("Result saved")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_IMAGE_LABEL = "extra_image_label"
        const val EXTRA_IMAGE_SCORE = "extra_image_score"
    }
}