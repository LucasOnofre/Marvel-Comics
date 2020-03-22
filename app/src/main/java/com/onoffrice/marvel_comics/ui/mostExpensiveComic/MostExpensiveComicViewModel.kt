package com.onoffrice.marvel_comics.ui.mostExpensiveComic

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.data.remote.model.ComicModel
import com.onoffrice.marvel_comics.utils.SingleLiveEvent
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
}