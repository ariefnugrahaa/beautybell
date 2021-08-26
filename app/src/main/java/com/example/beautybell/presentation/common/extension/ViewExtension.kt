package com.example.beautybell.presentation.common.extension

import android.view.View

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeEnable() {
    this.isEnabled = true
}

fun View.makeDisable() {
    this.isEnabled = false
}