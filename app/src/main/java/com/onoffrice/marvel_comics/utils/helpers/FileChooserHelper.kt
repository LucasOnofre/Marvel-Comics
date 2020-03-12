package com.onoffrice.marvel_comics.utils.helpers

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import java.io.File
import java.util.*
import java.util.regex.Pattern

class FileChooserHelper : PermissionDispatcherHelper.OnPermissionResult {

    private val mPermissionDispatcherHelper: PermissionDispatcherHelper
    private var mSelectorTitle: String? = null
    private var mOnFileChooserListener: OnFileChooserListener
    private var mType: String
    private var mActivity: Activity? = null
    private var mFragment: androidx.fragment.app.Fragment? = null

    constructor(activity: Activity, type: String = "*/*", onFileChooserListener: OnFileChooserListener) {
        mActivity = activity
        mType = type
        mOnFileChooserListener = onFileChooserListener
    }

    constructor(fragment: androidx.fragment.app.Fragment, type: String = "*/*", onFileChooserListener: OnFileChooserListener) {
        mActivity = fragment.activity
        mFragment = fragment
        mType = type
        mOnFileChooserListener = onFileChooserListener
    }

    init {
        mPermissionDispatcherHelper = if (isFromFragment()) {
            PermissionDispatcherHelper(mFragment!!, REQUEST_FILE_CHOOSER, STORAGE_PERMISSIONS, this)
        } else {
            PermissionDispatcherHelper(mActivity!!, REQUEST_FILE_CHOOSER, STORAGE_PERMISSIONS, this)
        }
    }

    private fun isFromFragment() = mFragment != null

    fun openChooser(title: String? = null) {
        mSelectorTitle = title
        mPermissionDispatcherHelper.dispatchPermissions()
    }

    private fun dispatchFileChooserIntent() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = mType
            }
        } else {
            Intent().apply {
                type = mType
                action = Intent.ACTION_GET_CONTENT
            }
        }

        if (isFromFragment()) {
            mFragment!!.startActivityForResult(Intent.createChooser(intent, mSelectorTitle
                    ?: "Select file"), REQUEST_FILE_CHOOSER
            )
        } else {
            mActivity!!.startActivityForResult(Intent.createChooser(intent, mSelectorTitle
                    ?: "Select file"), REQUEST_FILE_CHOOSER
            )
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_FILE_CHOOSER && resultCode == Activity.RESULT_OK) {
            handleResult(data)
        }
    }

    private fun handleResult(data: Intent?) {
        var realPath: String? = null

        if (data?.data != null) {

            var isImageFromGoogleDrive = false

            val uri = data.data

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(mActivity, uri)) {
                if ("com.android.externalstorage.documents" == uri?.authority) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]

                    if ("primary".equals(type, true)) {
                        realPath = "${Environment.getExternalStorageDirectory()}/${split[1]}"
                    } else {
                        val dirSeparator = Pattern.compile("/")
                        val rv = HashSet<Any?>()
                        val rawExternalStorage = System.getenv("EXTERNAL_STORAGE")
                        val rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE")
                        val rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET")
                        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
                            if (TextUtils.isEmpty(rawExternalStorage)) {
                                rv.add("/storage/sdcard0")
                            } else {
                                rv.add(rawExternalStorage)
                            }
                        } else {
                            var rawUserId = ""

                            val path = Environment.getExternalStorageDirectory().absolutePath
                            val folders = dirSeparator.split(path)
                            val lastFolder = folders[folders.size - 1]
                            var isDigit = false
                            try {
                                Integer.valueOf(lastFolder)
                                isDigit = true
                            } catch (ignored: NumberFormatException) {
                            }
                            rawUserId = if (isDigit) lastFolder else ""


                            if (TextUtils.isEmpty(rawUserId)) {
                                rv.add(rawEmulatedStorageTarget)
                            } else {
                                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId)
                            }
                        }
                        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                            val rawSecondaryStorages = rawSecondaryStoragesStr?.split(File.pathSeparator)
                            Collections.addAll(rv, rawSecondaryStorages)
                        }
                        val temp = arrayOfNulls<String>(rv.size)

                        for (i in 0..temp.size) {
                            val tempf = File(temp[i] + "/" + split[1])
                            if (tempf.exists()) {
                                realPath = temp[i] + "/" + split[1]
                            }
                        }
                    }
                } else if ("com.android.providers.downloads.documents" == uri?.authority) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), id.toLong())

                    var cursor: Cursor? = null
                    val column = "_data"
                    val projection = arrayOf(column)
                    try {
                        cursor = mActivity!!.contentResolver.query(contentUri, projection, null, null, null)
                        if (cursor != null && cursor.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndexOrThrow(column)
                            realPath = cursor.getString(columnIndex)
                        }
                    } finally {
                        cursor?.close()
                    }
                } else if ("com.android.providers.media.documents" == uri?.authority) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]

                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    var cursor: Cursor? = null
                    val column = "_data"
                    val projection = arrayOf(column)

                    try {
                        cursor = mActivity!!.contentResolver.query(contentUri!!, projection, selection, selectionArgs, null)
                        if (cursor != null && cursor.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndexOrThrow(column)
                            realPath = cursor.getString(columnIndex)
                        }
                    } finally {
                        cursor?.close()
                    }
                } else if ("com.google.android.apps.docs.storage" == uri?.authority) {
                    isImageFromGoogleDrive = true
                }
            } else if ("content".equals(uri?.scheme, true)) {
                var cursor: Cursor? = null
                val column = "_data"
                val projection = arrayOf(column)

                try {
                    cursor = mActivity!!.contentResolver.query(uri!!, projection, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(column)
                        realPath = cursor.getString(columnIndex)
                    }
                } finally {
                    cursor?.close()
                }
            } else if ("file".equals(uri?.scheme, true)) {
                realPath = uri?.path
            }

            try {
                when {
                    isImageFromGoogleDrive -> mOnFileChooserListener.onFileFailed(
                        ERROR_PERMISSION_DENIED, "Can't get file from Google Drive.")
                    realPath == null -> mOnFileChooserListener.onFileFailed(ERROR_FILE_NOT_FOUND, "File not found.")
                    else -> mOnFileChooserListener.onFileReady(File(realPath))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mOnFileChooserListener.onFileFailed(ERROR_UNKNOWN, "Unknown error.")
            }
        }
    }

    fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mPermissionDispatcherHelper.onRequestPermissionsResult(permsRequestCode, permissions, grantResults)
    }

    override fun onAllPermissionsGranted(requestCode: Int) {
        dispatchFileChooserIntent()
    }

    override fun onPermissionsDenied(requestCode: Int, deniedPermissions: List<String>, deniedPermissionsWithNeverAskAgainOption: List<String>) {
        if (deniedPermissionsWithNeverAskAgainOption.isNotEmpty()) {
            mOnFileChooserListener.onFileFailed(ERROR_PERMISSION_DENIED_WITH_NEVER_ASK_AGAIN, "Permission denied with never again option.")
        } else if (deniedPermissions.isNotEmpty()) {
            mOnFileChooserListener.onFileFailed(ERROR_PERMISSION_DENIED, "Permission denied.")
        } else {
            mOnFileChooserListener.onFileFailed(ERROR_UNKNOWN, "On permission dispatch error.")
        }
    }

    interface OnFileChooserListener {
        fun onFileReady(file: File?)

        fun onFileFailed(code: Int, msg: String)
    }

    companion object {
        //ERROR CODES
        const val ERROR_PERMISSION_DENIED = 0
        const val ERROR_PERMISSION_DENIED_WITH_NEVER_ASK_AGAIN = 1
        const val ERROR_FILE_NOT_FOUND = 2
        const val ERROR_FILE_GOOGLE_DRIVE = 3
        const val ERROR_UNKNOWN = 4
        //REQUEST CODES
        const val REQUEST_FILE_CHOOSER = 100
        //PERMISSIONS
        private val STORAGE_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}