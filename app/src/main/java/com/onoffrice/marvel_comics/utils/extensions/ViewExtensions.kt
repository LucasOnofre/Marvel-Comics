package com.onoffrice.marvel_comics.utils.extensions

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onoffrice.marvel_comics.R


fun View.setVisible(visible: Boolean, useInvisible: Boolean = false) {
    visibility = when {
        visible -> View.VISIBLE
        useInvisible -> View.INVISIBLE
        else -> View.GONE
    }
}

fun ImageView.loadImage(url: String?): Boolean {
    try {
        val optionsToApply = RequestOptions()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.placeholder_photo))
                .fitCenter()

        Glide.with(context).load(url).apply(optionsToApply).into(this)

    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}

/** Animates List item views
 * firstPositionDelay is necessary cause  first position also needs a delay, but it's 0 **/
fun View.fadeUpItemListAnimation(position: Int, animationDelay: Long) {
    val firstPositionDelay = 3
    val animation = AnimationUtils.loadAnimation(context, R.anim.fade_up_item)
    animation.startOffset = (position + firstPositionDelay) * animationDelay

    this.animation = animation
}

/** Animates List item views
 * firstPositionDelay is necessary cause first position also needs a delay, but it's 0 **/
fun View.fadeInItemListAnimation(position: Int, animationDelay: Long) {
    val firstPositionDelay = 0
    val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_item)
    animation.startOffset = (position + firstPositionDelay) * animationDelay

    this.animation = animation
}
