package com.dicoding.asclepius.helper

import java.text.NumberFormat

object NumberUtil {
    fun Float.toReadablePercentage(): String {
        return NumberFormat.getPercentInstance().format(this).trim()
    }
}