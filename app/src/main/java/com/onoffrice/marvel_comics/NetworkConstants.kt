package com.onoffrice.marvel_comics


object NetworkConstants {

    //API URLs
    val BASE_URL: String = BuildConfig.BASE_URL

    const val API_PUBLIC_KEY = "213ef1aad8e78ef69547947b85b00e91"
    const val API_PRIVATE_KEY = "1517ecd767aaa4ae5ba7dfc005377349d36dddc3"
    const val CHARACTER_LIMIT = 20

    val CHARACTERS = "${BASE_URL}v1/public/characters/"
}