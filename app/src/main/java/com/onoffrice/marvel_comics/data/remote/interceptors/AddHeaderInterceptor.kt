package com.onoffrice.marvel_comics.data.remote.interceptors

import androidx.multidex.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AddHeaderInterceptor : Interceptor {
    private val REQUEST_HEADER_AUTHENTICATION =
        listOf(
            RequestHeaderInterceptor(
                "Client-ID",
                ""
                //BuildConfig.CLIENT_ID_HEADER
            )
//            RequestHeaderInterceptor(
//                "Accept",
//                "" BuildConfig.API_VERSION_JSON_HEADER
//            )
        )

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        var request  = original

        //Adds every item on request header list on the request header
            request = original.newBuilder().also { request ->
                REQUEST_HEADER_AUTHENTICATION.forEach {
                    request.addHeader(it.name, it.value)
                }
            }
                .method(original.method(), original.body())
                .build()

        return chain.proceed(request)
    }
}