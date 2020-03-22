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

    val comic        = SingleLiveEvent<ComicModel>()
    val character    = SingleLiveEvent<Character>()
    val errorEvent   = SingleLiveEvent<String>()
    val loadingEvent = SingleLiveEvent<Boolean>()

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
                    handleCharacterComicsResponse(it.characterComicsData.comics)
                },
                onError = {
                    errorEvent.value = it.message
                }
            ))
        }
    }

    private fun handleCharacterComicsResponse(comics: List<ComicModel>) {
        comic.value = comics[0]
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}