package com.onoffrice.marvel_comics.data.remote.datasource

import com.onoffrice.marvel_comics.NetworkConstants
import com.onoffrice.marvel_comics.data.remote.interceptors.AddHeaderInterceptor
import com.onoffrice.marvel_comics.data.request.RetrofitSingle
import com.onoffrice.marvel_comics.data.request.services.ComicsService


object GamesDataSource {

    private val service = RetrofitSingle.createService(
        url          = NetworkConstants.GAMES,
        serviceClass = ComicsService::class.java,
        interceptors = listOf(AddHeaderInterceptor())
    )

   // fun getTopGames(page: Int) = service.getTopGames(page)

}