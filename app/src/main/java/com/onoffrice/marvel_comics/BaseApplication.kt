package com.onoffrice.marvel_comics

import androidx.multidex.MultiDexApplication
import com.onoffrice.marvel_comics.data.di.KoinInjector
import com.onoffrice.marvel_comics.data.local.PreferencesHelper
import com.onoffrice.marvel_comics.data.remote.RemotePreferencesHelper
import com.onoffrice.marvel_comics.data.remote.UserUnauthorizedBus
import com.onoffrice.marvel_comics.utils.extensions.observableSubscribe
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        setPreferencesHelper()
        setBus()

        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@BaseApplication)

            modules(
                listOf(
                    KoinInjector.charactersModule,
                    KoinInjector.charactersDetailModule,
                    KoinInjector.mostExpensiveComicModule
                )
            )
        }
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
