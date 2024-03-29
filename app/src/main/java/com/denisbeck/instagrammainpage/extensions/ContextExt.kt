package com.denisbeck.instagrammainpage.extensions

import android.content.Context

fun Context.dpToPx(dp: Int): Int {
    val d: Float = this.resources.displayMetrics.density
    return (dp * d).toInt()
}