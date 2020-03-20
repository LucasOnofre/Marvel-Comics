package com.onoffrice.marvel_comics.data.repositories


import com.onoffrice.marvel_comics.NetworkConstants
import com.onoffrice.marvel_comics.data.remote.interceptors.AddHeaderInterceptor
import com.onoffrice.marvel_comics.data.remote.model.CharacterDataWrapper
import com.onoffrice.marvel_comics.data.remote.model.MarvelCharacterComicsDataWrapper
import com.onoffrice.marvel_comics.data.request.RetrofitSingle
import com.onoffrice.marvel_comics.data.request.services.MarvelService
import io.reactivex.Single

interface CharactersRepository {
    fun getCharacters(limitRegister: Int, offset: Int): Single<CharacterDataWrapper>
    fun getCharacterComics(characterId: Int): Single<MarvelCharacterComicsDataWrapper>
}

class CharactersRepositoryImp: CharactersRepository {

    private val service = RetrofitSingle.createService(
        url             = NetworkConstants.BASE_URL,
        serviceClass    = MarvelService::class.java,
        interceptors    = listOf(AddHeaderInterceptor())
    )

    override fun getCharacters(limitRegister: Int, offset: Int) = service.getCharacters(limitRegister, offset)

    override fun getCharacterComics(characterId: Int) = service.getCharacterComics(characterId)

}

