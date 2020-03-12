package com.onoffrice.marvel_comics

import androidx.multidex.BuildConfig

object NetworkConstants {

    //1xx Informational
    const val CODE_WITHOUT_NETWORK = 0

    //2xx Success
    const val CODE_RESPONSE_SUCCESS = 200

    //3xx Redirection
    const val CODE_NOT_FOUND = 340

    //4xx Client Error
    const val CODE_TIMEOUT = 408
    const val CODE_RESPONSE_UNAUTHORIZED = 401
    const val CODE_BAD_REQUEST = 400
    const val CODE_FORBIDDEN = 403

    //5xx Server Error
    const val CODE_UNKNOWN = 500

    //COMMON FACEBOOK PERMISSIONS
    val FACEBOOK_PERMISSIONS = arrayOf("public_profile", "email")
    const val FACEBOOK_REQUEST_KEY = "fields"
    const val FACEBOOK_REQUEST_VALUE = "id, first_name, last_name, email"

    //API URLs
    //val BASE_URL: String = BuildConfig.BASE_URL
    val BASE_URL: String = ""

    val GAMES = "${BASE_URL}games/"


}