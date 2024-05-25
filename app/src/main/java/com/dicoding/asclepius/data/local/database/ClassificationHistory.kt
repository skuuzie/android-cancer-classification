package com.dicoding.asclepius.data.local.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "classification_history")
@Parcelize
data class ClassificationHistory(
    @PrimaryKey
    @ColumnInfo(name = "image_uri")
    var imageUri: String,

    @ColumnInfo(name = "classification_label") var classificationLabel: String,
    @ColumnInfo(name = "classification_score") var classificationScore: Float
) : Parcelable