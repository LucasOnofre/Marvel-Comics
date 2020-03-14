package com.onoffrice.marvel_comics.data.repositories


import com.onoffrice.marvel_comics.NetworkConstants
import com.onoffrice.marvel_comics.data.remote.interceptors.AddHeaderInterceptor
import com.onoffrice.marvel_comics.data.request.RetrofitSingle
import com.onoffrice.marvel_comics.data.request.services.MarvelService
import io.reactivex.Single


object CharactersRepository {

    private val service = RetrofitSingle.createService(
        url          = NetworkConstants.BASE_URL,
        serviceClass = MarvelService::class.java,
        interceptors = listOf(AddHeaderInterceptor())
    )

    fun getCharacters(limitRegister: Int, offset: Int) = service.getCharacters(limitRegister, offset)

}

