package com.onoffrice.marvel_comics.data.remote

import android.content.Context
import android.content.SharedPreferences
object RemotePreferencesHelper {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private const val SHARED_PREFERENCES_NAME = "NETWORKING.SHARED_PREFERENCES"

    private const val PREF_SESSION_COOKIE = "$SHARED_PREFERENCES_NAME.PREF_SESSION_COOKIE"
    private const val PREF_BASIC_AUTH = "$SHARED_PREFERENCES_NAME.PREF_SESSION_COOKIE"

    var sessionCookie: String?
        get() = sharedPreferences.getString(PREF_SESSION_COOKIE, "")
        set(value) = sharedPreferences.edit().putString(PREF_SESSION_COOKIE, value).apply()

    var basicAuth: String?
        get() = sharedPreferences.getString(PREF_BASIC_AUTH, "")
        set(value) = sharedPreferences.edit().putString(PREF_BASIC_AUTH, value).apply()

    fun clearSharedPref() {
        sharedPreferences.edit().clear().apply()
    }
}