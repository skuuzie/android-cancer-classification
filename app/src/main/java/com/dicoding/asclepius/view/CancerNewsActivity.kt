package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityCancerNewsBinding
import com.dicoding.asclepius.view.adapter.CancerNewsAdapter
import com.dicoding.asclepius.view.model.CancerNewsModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CancerNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCancerNewsBinding
    private lateinit var adapter: CancerNewsAdapter

    private val cancerNewsModel: CancerNewsModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancerNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CancerNewsAdapter()

        with(binding) {
            topAppBar.setNavigationOnClickListener {
                super.onBackPressedDispatcher.onBackPressed()
            }
            rvNews.layoutManager = LinearLayoutManager(this@CancerNewsActivity)
            rvNews.setHasFixedSize(true)
            rvNews.adapter = adapter
        }

        setupIndicator()
        showNews()
    }

    private fun showNews() {
        cancerNewsModel.fetchNews().observe(this) { newsList ->
            adapter.setNewsList(newsList)
            binding.rvNews.adapter = adapter

            adapter.setOnItemClickCallback(object : CancerNewsAdapter.OnItemClickCallback {
                override fun onVisitClicked(news: ArticlesItem) {
                    val webpage = Uri.parse(news.url)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    startActivity(intent)
                }
            })
        }
    }

    private fun setupIndicator() {
        cancerNewsModel.isLoading.observe(this) {
            if (it) showLoadingIndicator() else {
                hideLoadingIndicator()
            }
        }

        cancerNewsModel.isFailure.observe(this) {
            if (it) showErrorMessage() else {
                hideErrorMessage()
            }
        }
    }

    private fun showLoadingIndicator() {
        binding.circularProgress.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        binding.circularProgress.visibility = View.GONE
    }

    private fun showErrorMessage() {
        binding.tvMessage.text = getString(R.string.network_error_message)
        binding.tvMessage.visibility = View.VISIBLE
        binding.tvMessage.setOnClickListener {
            showNews()
        }
    }

    private fun hideErrorMessage() {
        binding.tvMessage.text = ""
        binding.tvMessage.visibility = View.GONE
        binding.tvMessage.setOnClickListener(null)
    }
}