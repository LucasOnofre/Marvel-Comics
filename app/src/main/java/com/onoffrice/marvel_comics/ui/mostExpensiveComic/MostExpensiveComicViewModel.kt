package com.onoffrice.marvel_comics.ui.mostExpensiveComic

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.data.remote.model.ComicModel
import com.onoffrice.marvel_comics.data.repositories.CharactersRepository
import com.onoffrice.marvel_comics.ui.characterDetail.CharacterDetailViewModel
import com.onoffrice.marvel_comics.utils.SingleLiveEvent
import com.onoffrice.marvel_comics.utils.extensions.singleSubscribe
import io.reactivex.disposables.CompositeDisposable

class MostExpensiveComicViewModel: ViewModel() {

    val comic        = SingleLiveEvent<ComicModel>()
    val errorEvent   = SingleLiveEvent<String>()
    val loadingEvent = SingleLiveEvent<Boolean>()

    private val disposable = CompositeDisposable()

    fun getExtras(extras: Bundle?){
        comic.value =  extras?.getSerializable(Constants.EXTRA_COMIC) as ComicModel
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MostExpensiveComicViewModel() as T
        }
    }
}