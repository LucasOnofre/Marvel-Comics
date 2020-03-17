package com.onoffrice.marvel_comics.ui.characterDetail

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.data.repositories.CharactersRepository
import com.onoffrice.marvel_comics.ui.characters.CharactersViewModel
import com.onoffrice.marvel_comics.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

class CharacterDetailViewModel(private val repository: CharactersRepository) : ViewModel() {

    val character   = SingleLiveEvent<Character>()
    val errorEvent   = SingleLiveEvent<String>()
    val loadingEvent = SingleLiveEvent<Boolean>()

     private val disposable = CompositeDisposable()

    fun getExtras(extras: Bundle?){
       character.value =  extras?.getSerializable(Constants.EXTRA_CHARACTER_DETAIL) as Character
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