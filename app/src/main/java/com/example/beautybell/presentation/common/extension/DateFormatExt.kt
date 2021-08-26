package com.example.beautybell.presentation.common.extension

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun Context.dateFormatter(date: String): String {
    val baseFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val secondFormatter = SimpleDateFormat("dd MMMM yyyy, hh:mm", Locale.ENGLISH)
    return secondFormatter.format(baseFormatter.parse(date))
}