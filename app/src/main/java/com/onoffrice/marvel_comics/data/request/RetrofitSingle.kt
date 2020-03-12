package com.onoffrice.marvel_comics.data.request

import androidx.multidex.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitSingle{

    /** Singleton to instanciate the Retrofit in all the application **/
    fun <S> createService(serviceClass: Class<S>, interceptors: List<Interceptor>? = null, url: String): S {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(
            HttpLoggingInterceptor()
            .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))

        interceptors?.let {
            for (interceptor in interceptors) {
                httpClient.addInterceptor(interceptor)
            }
        }
        retrofit.client(httpClient.build())
        return retrofit.build().create(serviceClass)
    }
}
