package com.onoffrice.marvel_comics.data.request

import androidx.multidex.BuildConfig
import com.onoffrice.marvel_comics.NetworkConstants
import com.onoffrice.marvel_comics.data.remote.hashGenerator.MarvelHashGenerate
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


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
                httpClient.addInterceptor({ chain -> createParametersDefault(chain) })
            }
        }
        retrofit.client(httpClient.build())
        return retrofit.build().create(serviceClass)
    }

    private fun createParametersDefault(chain: Interceptor.Chain): Response {
        val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        var request = chain.request()
        val builder = request.url().newBuilder()

        builder.addQueryParameter("apikey", NetworkConstants.API_PUBLIC_KEY)
            .addQueryParameter("hash", MarvelHashGenerate.generate(timeStamp, NetworkConstants.API_PRIVATE_KEY, NetworkConstants.API_PUBLIC_KEY))
            .addQueryParameter("ts", timeStamp.toString())

        request = request.newBuilder().url(builder.build()).build()
        return chain.proceed(request)

    }
}
