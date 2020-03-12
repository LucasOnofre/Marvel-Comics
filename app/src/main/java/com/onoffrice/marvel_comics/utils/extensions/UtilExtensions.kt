package com.onoffrice.marvel_comics.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.onoffrice.marvel_comics.Constants
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.*
import java.util.*
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


fun Context.getColorRes(idRes: Int): Int {
    return ContextCompat.getColor(this, idRes)
}

fun Context.getDrawableCompat(@DrawableRes drawableResId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableResId)
}

@Deprecated("Use makeCall() from anko instead")
fun Context.callPhone(phone: String): Boolean {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}

fun Context.navigateIntent(address: String = "", latitude: String = "0", longitude: String = "0") {
    val gmmIntentUri = "geo:$latitude,$longitude?q=$address"
    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(gmmIntentUri))
    startActivity(mapIntent)
}

fun Context.notImplementedFeature(msg: String? = null) {
    val fullMsg = if (msg != null) {
        "Função não implementada - " + msg
    } else {
        "Função não implementada"
    }
    this.showToast(fullMsg)
}

fun Context.newColorStateList(color: Int): ColorStateList {
    return ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_pressed), //1
                    intArrayOf(android.R.attr.state_focused), //2
                    intArrayOf(android.R.attr.state_focused, android.R.attr.state_pressed) //3
            ),
            intArrayOf(color, color, color)
    )
}

fun Context.createEventIntent(title: String, dateInMillis: Long = 0, allDay: Boolean = false) {
    val date = Calendar.getInstance()
    date.timeInMillis = dateInMillis

    val intent = Intent(Intent.ACTION_EDIT)

    intent.type = "vnd.android.cursor.item/event"
    intent.putExtra("title", title)
    intent.putExtra("beginTime", date.timeInMillis)
    intent.putExtra("endTime", date.timeInMillis + (60 * 60 * 1000))
    intent.putExtra("allDay", allDay)
    startActivity(intent)
}

fun rand(from: Int, to: Int): Int {
    return Random().nextInt(to - from) + from
}


fun Context.shareFile(file: File) {
    val uri = FileProvider.getUriForFile(this,
            Constants.PACKAGE_NAME + ".fileprovider",
            file)
    val share = Intent()
    share.action = Intent.ACTION_SEND
    share.type = contentResolver.getType(uri)
    share.putExtra(Intent.EXTRA_STREAM, uri)
    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(share)
}

/**
 * Calls the specified function [block] and if it does not crash returns its result, else returns null.
 */
@kotlin.contracts.ExperimentalContracts
fun <T> tryOrNull(block: () -> T): T? {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return try {
        block.invoke()
    } catch (e: Exception) {
        null
    }
}

/**
 * An extension that converts a ResponseBody object to a file so that we can manipulate it, open it, share it and so on.
 * @param fileName The name that will be given to the file
 * @return The file that has been created, or null if some error happend when creating the file
 */
fun ResponseBody.toFile(fileName: String): File? {
    try {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            val fileReader = ByteArray(4096)

            var fileSizeDownloaded = 0

            inputStream = this.byteStream()
            outputStream = FileOutputStream(file)

            while (true) {
                val read = inputStream.read(fileReader)

                if (read == -1) {
                    break
                }

                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read

            }

            outputStream.flush()
            return file

        } catch (e: IOException) {
            return null
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    } catch (e: IOException) {
        return null
    }
}

fun createPart(file: File, mimeType: String, paramMultipart: String): Single<MultipartBody.Part> = Single.create<MultipartBody.Part> { emitter ->
    try {
        val requestFile = RequestBody.create(MediaType.parse(mimeType), file)
        emitter.onSuccess(MultipartBody.Part.createFormData(paramMultipart, file.name, requestFile))
    } catch (e: Exception) {
        emitter.onError(e)
    }
}