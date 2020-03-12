package com.onoffrice.marvel_comics.utils.extensions

import android.content.Intent

fun Intent.getString(key: String): String {
    return getStringExtra(key) ?: ""
}