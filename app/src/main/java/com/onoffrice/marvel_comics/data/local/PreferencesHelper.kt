package com.onoffrice.marvel_comics.data.local

import android.content.Context
import android.content.SharedPreferences
import com.onoffrice.marvel_comics.Constants.PACKAGE_NAME

object PreferencesHelper {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private const val SHARED_PREFERENCES_NAME = "$PACKAGE_NAME.SHARED_PREFERENCES"

    private const val PREF_IS_ONLINE = "$SHARED_PREFERENCES_NAME.PREF_IS_ONLINE"


    var isOnline: Boolean
        get() = sharedPreferences.getBoolean(PREF_IS_ONLINE, true)
        set(value) = sharedPreferences.edit().putBoolean(PREF_IS_ONLINE, value).apply()


//    /** Saves the last top games response **/
//    var games: TopGamesList?
//        get() {
//            val userJson = sharedPreferences.getString(PREF_BANNERS, "")
//            return Gson().fromJson(userJson, TopGamesList::class.java) ?: return TopGamesList()
//        }
//        set(value) {
//            val banners = value ?: TopGamesList()
//            val json = Gson().toJson(banners, TopGamesList::class.java)
//            sharedPreferences.edit().putString(PREF_BANNERS, json).apply()
//            isOnline = true
//        }
}