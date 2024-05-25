package com.dicoding.asclepius.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.database.ClassificationHistory
import com.dicoding.asclepius.databinding.ItemClassificationHistoryBinding
import com.dicoding.asclepius.helper.NumberUtil.toReadablePercentage

class ClassificationHistoryAdapter :
    RecyclerView.Adapter<ClassificationHistoryAdapter.MyViewHolder>() {
    private val historyList = ArrayList<ClassificationHistory>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(val binding: ItemClassificationHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ClassificationHistory) {
            with(binding) {
                imageResult.setImageURI(Uri.parse(result.imageUri))
                textLabelResult.text = result.classificationLabel
                textScoreResult.text = result.classificationScore.toReadablePercentage()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemClassificationHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(historyList[position])
        holder.binding.itemContainer.setOnClickListener {
            onItemClickCallback.onItemClicked(historyList[position])
        }
        holder.binding.btnDelete.setOnClickListener {
            onItemClickCallback.onDeleteClicked(historyList[position])
            deleteClassification(position)
        }
    }

    fun setHistoryList(historyList: List<ClassificationHistory>?) {
        this.historyList.clear()
        if (historyList != null) {
            this.historyList.addAll(historyList)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun deleteClassification(position: Int) {
        this.historyList.removeAt(position)
        notifyItemRemoved(position)
    }

    interface OnItemClickCallback {
        fun onItemClicked(result: ClassificationHistory)
        fun onDeleteClicked(result: ClassificationHistory)
    }
}