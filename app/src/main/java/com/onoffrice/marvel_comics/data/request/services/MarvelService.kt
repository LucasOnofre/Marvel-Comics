package com.onoffrice.marvel_comics.data.request.services

import com.onoffrice.marvel_comics.data.remote.model.MarvelCharactersResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {

    @GET("characters")
    fun getCharacters(@Query("limit") limit: Int, @Query("offset") offset: Int): Single<MarvelCharactersResponse>


//    @GET("characters/{characterId}/comics")
//    fun getCharacterComics(@Query("limit") limit: Int, @Query("offset") offset: Int): Single<MarvelCharacterComicsResponse>

}