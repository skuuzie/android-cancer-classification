package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class ImageClassifierHelper(
    var modelName: String = "cancer_classification.tflite",
    var threshold: Float = 0.5f,
    var numThreads: Int = 4,
    var maxResults: Int = 3,
    val ctx: Context,
    val classifierListener: ClassifierListener?
) {
    private val TAG = "ImageClassifierHelper"
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptions = BaseOptions.builder()
            .setNumThreads(numThreads)
            .build()

        optionsBuilder.setBaseOptions(baseOptions)

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                ctx,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(ctx.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val imageStream = ctx.contentResolver.openInputStream(imageUri)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .build()

        val tensorImage =
            imageProcessor.process(TensorImage.fromBitmap(BitmapFactory.decodeStream(imageStream)))
        imageStream?.close()

        var inferenceTime = SystemClock.uptimeMillis()
        val results = imageClassifier?.classify(tensorImage)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        classifierListener?.onResults(
            results,
            inferenceTime
        )
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: MutableList<Classifications>?,
            inferenceTime: Long
        )
    }

}