package com.onoffrice.marvel_comics.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Handler
import android.widget.Toast
import com.onoffrice.marvel_comics.R

fun Context.isNetworkConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}

fun Activity.startActivitySlideTransition(intent: Intent, requestCode: Int? = null) {
    startActivityTransition(intent, R.anim.anim_close_scale, R.anim.slide_in_left, 1, requestCode)
}

fun Activity.startActivityFadeTransition(intent: Intent, requestCode: Int? = null) {
    startActivityTransition(intent, R.anim.anim_fade_out, R.anim.anim_fade_in, 1, requestCode)
}

fun Activity.startActivityTransition(intent: Intent, idAnimationOut: Int,
                                     idAnimationIn: Int, delay: Long, requestCode: Int? = null) {
    if (requestCode == null) {
        Handler().postDelayed({
            this.startActivity(intent)
            this.overridePendingTransition(idAnimationIn, idAnimationOut)
        }, delay)
    } else {
        Handler().postDelayed({
            this.startActivityForResult(intent, requestCode)
            this.overridePendingTransition(idAnimationIn, idAnimationOut)
        }, delay)
    }
}

fun Activity.finishWithSlideTransition() {
    finish()
    overridePendingTransition(R.anim.anim_open_scale, R.anim.slide_out_right)
}

fun Activity.finishWithFadeTransition() {
    finish()
    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
}

fun Activity.finishWithTransition(idAnimationOut: Int, idAnimationIn: Int, delay: Long) {
    Handler().postDelayed({
        this.finish()
        this.overridePendingTransition(idAnimationIn, idAnimationOut)
    }, delay)
}

//TOAST METHODS
fun Context.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

fun Context.showErrorToast(msg: String?) {
    showLongToast(msg ?: getString(R.string.common_error))
}