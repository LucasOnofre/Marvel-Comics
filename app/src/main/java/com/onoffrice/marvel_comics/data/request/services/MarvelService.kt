package com.onoffrice.marvel_comics.data.request.services

import com.onoffrice.marvel_comics.data.remote.model.CharacterDataWrapper
import com.onoffrice.marvel_comics.data.remote.model.MarvelCharacterComicsDataWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelService {

    @GET("characters")
    fun getCharacters(@Query("limit") limit: Int, @Query("offset") offset: Int): Single<CharacterDataWrapper>

    @GET("characters/{characterId}/comics")
   fun getCharacterComics(@Path ("characterId") characterId: Int): Single<MarvelCharacterComicsDataWrapper>

}