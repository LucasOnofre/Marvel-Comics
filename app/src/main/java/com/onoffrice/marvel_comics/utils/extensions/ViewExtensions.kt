package com.onoffrice.marvel_comics.utils.extensions

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.onoffrice.marvel_comics.R


fun View.setVisible(visible: Boolean, useInvisible: Boolean = false) {
    visibility = when {
        visible -> View.VISIBLE
        useInvisible -> View.INVISIBLE
        else -> View.GONE
    }
}

fun ImageView.loadImage(url: String?, options: RequestOptions? = null, simpleTarget: SimpleTarget<Drawable>? = null): Boolean {
    try {
        val optionsToApply = options ?: RequestOptions()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.placeholder_photo))
                .fitCenter()
        if (simpleTarget == null) {
            Glide.with(context).load(url).apply(optionsToApply).into(this)
        } else {
            Glide.with(context).load(url).apply(optionsToApply).into(simpleTarget)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}