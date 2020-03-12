package com.onoffrice.marvel_comics

import androidx.multidex.MultiDexApplication
import com.onoffrice.marvel_comics.utils.extensions.observableSubscribe
import com.onoffrice.marvel_comics.data.local.PreferencesHelper
import com.onoffrice.marvel_comics.data.remote.RemotePreferencesHelper
import com.onoffrice.marvel_comics.data.remote.UserUnauthorizedBus

class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        setPreferencesHelper()
        setBus()
    }

    private fun setPreferencesHelper() {
        PreferencesHelper.init(applicationContext)
        RemotePreferencesHelper.init(applicationContext)
    }

    private fun setBus() {
        UserUnauthorizedBus.getEvents().observableSubscribe(onNext = {
            startAuthenticationActivity()
        })
    }

    private fun startAuthenticationActivity() {
        // Todo startActivity()
    }

}
