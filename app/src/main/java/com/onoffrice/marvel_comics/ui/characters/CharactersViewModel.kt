package com.onoffrice.marvel_comics.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onoffrice.marvel_comics.data.remote.model.CharactersInfo
import com.onoffrice.marvel_comics.data.repositories.CharactersRepository
import com.onoffrice.marvel_comics.utils.SingleLiveEvent
import com.onoffrice.marvel_comics.utils.extensions.singleSubscribe
import io.reactivex.disposables.CompositeDisposable

var LIMIT_REGISTER = 20

class CharactersViewModel(private val repository: CharactersRepository) : ViewModel() {

    val characters =  SingleLiveEvent<List<CharactersInfo>>()
    val loadingEvent = SingleLiveEvent<Boolean>()
    val errorEvent = SingleLiveEvent<String>()
//
     private val disposable = CompositeDisposable()
//
//
    fun getCharacters(offset: Int) {
        disposable.add(repository.getCharacters(LIMIT_REGISTER, offset).singleSubscribe(
                onLoading = {
                  //  loadingEvent.value = it
                },
                onSuccess = {
                    characters.value = it.charactersData.charactersInfo
                },
                onError = {
                    errorEvent.value = it.message
                }))
    }


    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    class Factory(private val repository: CharactersRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CharactersViewModel(repository) as T
        }
    }
}