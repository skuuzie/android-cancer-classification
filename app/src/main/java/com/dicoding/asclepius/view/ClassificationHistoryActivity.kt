package com.dicoding.asclepius.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.local.database.ClassificationHistory
import com.dicoding.asclepius.databinding.ActivityClassificationHistoryBinding
import com.dicoding.asclepius.view.adapter.ClassificationHistoryAdapter
import com.dicoding.asclepius.view.model.ClassificationHistoryModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassificationHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassificationHistoryBinding
    private lateinit var adapter: ClassificationHistoryAdapter

    private val classificationHistoryModel: ClassificationHistoryModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassificationHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ClassificationHistoryAdapter()

        with(binding) {
            topAppBar.setNavigationOnClickListener {
                super.onBackPressedDispatcher.onBackPressed()
            }
            rvClassificationHistory.layoutManager =
                LinearLayoutManager(this@ClassificationHistoryActivity)
            rvClassificationHistory.adapter = adapter
        }

        showHistory()
    }

    private fun showHistory() {
        showLoadingIndicator()

        classificationHistoryModel.histories.observe(this) { historyList ->
            hideLoadingIndicator()
            adapter.setHistoryList(historyList)

            if (historyList.isNullOrEmpty()) {
                showEmptyMessage()
            } else {
                binding.rvClassificationHistory.adapter = adapter
            }

            adapter.setOnItemClickCallback(object :
                ClassificationHistoryAdapter.OnItemClickCallback {
                override fun onItemClicked(result: ClassificationHistory) {
                    val intent =
                        Intent(this@ClassificationHistoryActivity, ResultActivity::class.java)
                            .apply {
                                putExtra(ResultActivity.EXTRA_IMAGE_URI, result.imageUri)
                                putExtra(
                                    ResultActivity.EXTRA_IMAGE_LABEL,
                                    result.classificationLabel
                                )
                                putExtra(
                                    ResultActivity.EXTRA_IMAGE_SCORE,
                                    result.classificationScore
                                )
                            }
                    startActivity(intent)
                }

                override fun onDeleteClicked(result: ClassificationHistory) {
                    classificationHistoryModel.delete(result)
                    adapter.setHistoryList(historyList)
                }

            })
        }
    }

    private fun showEmptyMessage() {
        binding.tvEmptyHistory.visibility = View.VISIBLE
    }

    private fun showLoadingIndicator() {
        binding.circularProgress.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        binding.circularProgress.visibility = View.GONE
    }

}