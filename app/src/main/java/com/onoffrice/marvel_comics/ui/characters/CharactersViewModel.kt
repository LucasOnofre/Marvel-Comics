package com.onoffrice.marvel_comics.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable

class CharactersViewModel(private val repository: FaqRepository) : ViewModel() {

    val questions =  SingleLiveEvent<List<FaqQuestion>>()
    val openContactUsActivity = SingleLiveEvent<Void>()
    val loadingEvent = SingleLiveEvent<Boolean>()
    val errorEvent = SingleLiveEvent<String>()

    private val disposable = CompositeDisposable()


    fun getFaq() {
        disposable.add(repository.getFaq().singleSubscribe(
                onLoading = {
                    loadingEvent.value = it
                },
                onSuccess = {
                    val faqsList = arrayListOf<FaqQuestion>()
                    it.forEach { question -> faqsList.add(question) }
                    questions.value = faqsList
                },
                onError = {
                    errorEvent.value = it.message
                }))
    }

    fun onClickDidNotFind() {
        openContactUsActivity.call()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

//    class Factory(private val repository: FaqRepository) : ViewModelProvider.NewInstanceFactory() {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            @Suppress("UNCHECKED_CAST")
//            return FaqViewModel(repository) as T
//        }
//    }
}