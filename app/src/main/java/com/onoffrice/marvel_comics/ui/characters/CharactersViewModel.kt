package com.onoffrice.marvel_comics.ui.characters

import androidx.lifecycle.ViewModel
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.data.repositories.CharactersRepository
import com.onoffrice.marvel_comics.utils.SingleLiveEvent
import com.onoffrice.marvel_comics.utils.extensions.singleSubscribe
import io.reactivex.disposables.CompositeDisposable

var LIMIT_REGISTER = 20

class CharactersViewModel(private val repository: CharactersRepository) : ViewModel() {

    val characters   =  SingleLiveEvent<List<Character>>()
    val errorEvent   = SingleLiveEvent<String>()
    val loadingEvent = SingleLiveEvent<Boolean>()

     private val disposable = CompositeDisposable()

    fun getCharacters(offset: Int?) {
        disposable.add(repository.getCharacters(LIMIT_REGISTER, offset ?: 0).singleSubscribe(
                onLoading = {
                    loadingEvent.value = it
                },
                onSuccess = {
                    val charactersResponse = it.charactersData.characters
                    charactersResponse.let { list ->
                       if (!list.isNullOrEmpty()) {
                          characters.value = list
                       } else {
                           errorEvent.value = "Erro inexperado"
                       }
                    }
                },
                onError = {
                    errorEvent.value = it.message
                }))
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}