package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding

class CancerNewsAdapter : RecyclerView.Adapter<CancerNewsAdapter.MyViewHolder>() {
    private val newsList = ArrayList<ArticlesItem>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: ArticlesItem) {
            with(binding) {
                if (!news.urlToImage.isNullOrBlank()) {
                    Glide.with(binding.root)
                        .load(news.urlToImage)
                        .into(binding.newsImage)
                }
                newsTitle.text = news.title
                newsDescription.text = news.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(newsList[position])
        holder.binding.card.setOnClickListener {
            onItemClickCallback.onVisitClicked(newsList[position])
        }
    }

    fun setNewsList(newsList: List<ArticlesItem>?) {
        this.newsList.clear()
        if (newsList != null) {
            this.newsList.addAll(newsList.filter { it.title != "[Removed]" })
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onVisitClicked(news: ArticlesItem)
    }
}