package com.onoffrice.marvel_comics.ui.characterDetail

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.data.remote.model.ComicModel
import com.onoffrice.marvel_comics.data.remote.model.Price
import com.onoffrice.marvel_comics.data.repositories.CharactersRepository
import com.onoffrice.marvel_comics.utils.SingleLiveEvent
import com.onoffrice.marvel_comics.utils.extensions.singleSubscribe
import io.reactivex.disposables.CompositeDisposable

class CharacterDetailViewModel(private val repository: CharactersRepository) : ViewModel() {

    private val comicPrices: MutableList<ComicModel> = mutableListOf()

    val character       = SingleLiveEvent<Character>()
    val errorEvent      = SingleLiveEvent<String>()
    val loadingEvent    = SingleLiveEvent<Boolean>()
    val comic = SingleLiveEvent<ComicModel>()


     private val disposable = CompositeDisposable()

    fun getExtras(extras: Bundle?){
       character.value =  extras?.getSerializable(Constants.EXTRA_CHARACTER_DETAIL) as Character
    }

    fun getCharacterComics() {
        character.value?.id?.let { characterId ->
            disposable.add(repository.getCharacterComics(characterId).singleSubscribe(
                onLoading = {
                    loadingEvent.value = it
                },
                onSuccess = {
                   // handleCharacterComicsResponse(it.characterComicsData.comics)
                    comic.value = it.characterComicsData.comics[0]
                },
                onError = {
                    errorEvent.value = it.message
                }
            ))
        }
    }

    private fun handleCharacterComicsResponse(comics: List<ComicModel>) {

    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }


    class Factory(private val repository: CharactersRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CharacterDetailViewModel(repository) as T
        }
    }
}