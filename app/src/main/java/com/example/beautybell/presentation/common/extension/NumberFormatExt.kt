package com.example.beautybell.presentation.common.extension

import android.content.Context
import java.text.DecimalFormat

fun Context.numberFormatter(number: Int): String? {
    val numberFormat = DecimalFormat("#,###.##")
    return numberFormat.format(number)
}