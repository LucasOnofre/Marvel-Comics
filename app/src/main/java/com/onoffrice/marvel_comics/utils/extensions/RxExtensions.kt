package com.onoffrice.marvel_comics.utils.extensions

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

fun <T> Single<T>.singleSubscribe(
        onLoading: ((t: Boolean) -> Unit)? = null,
        onSuccess: ((t: T) -> Unit)? = null,
        onError: ((e: Throwable) -> Unit)? = null,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        observeOnScheduler: Scheduler? = AndroidSchedulers.mainThread()): DisposableSingleObserver<T> {
    
    onLoading?.let { it(true) }
    
    return this.subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribeWith(object : DisposableSingleObserver<T>() {
                override fun onSuccess(t: T) {
                    onLoading?.let { it(false) }
                    onSuccess?.let { it(t) }
                }
                
                override fun onError(e: Throwable) {
                    onLoading?.let { it(false) }
                    onError?.let { it(e) }
                }
            })
}

fun <T> Observable<T>.observableSubscribe(
        onLoading: ((t: Boolean) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onNext: ((t: T) -> Unit)? = null,
        onError: ((e: Throwable) -> Unit)? = null,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        observeOnScheduler: Scheduler? = AndroidSchedulers.mainThread()): DisposableObserver<T> {
    
    onLoading?.let { it(true) }
    
    return this.subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribeWith(object : DisposableObserver<T>() {
                override fun onNext(t: T) {
                    onLoading?.let { it(false) }
                    onNext?.let { it(t) }
                }
                
                override fun onComplete() {
                    onLoading?.let { it(false) }
                    onComplete?.let { it() }
                }
                
                override fun onError(e: Throwable) {
                    onLoading?.let { it(false) }
                    onError?.let { it(e) }
                }
            })
}


fun Completable.completableSubscribe(
        onLoading: ((t: Boolean) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onError: ((e: Throwable) -> Unit)? = null,
        subscribeOnScheduler: Scheduler? = Schedulers.io(),
        observeOnScheduler: Scheduler? = AndroidSchedulers.mainThread()): DisposableCompletableObserver {
    
    onLoading?.let { it(true) }
    
    return this.subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    onLoading?.let { it(false) }
                    onComplete?.let { it() }
                }
                
                override fun onError(e: Throwable) {
                    onLoading?.let { it(false) }
                    onError?.let { it(e) }
                }
            })
    
}


fun <T> Flowable<T>.flowableSubscribe(onNext: ((t: T) -> Unit)? = null, onError: ((e: Throwable?) -> Unit)? = null,
                                      onComplete: (() -> Unit)? = null, subscribeOnScheduler: Scheduler? = Schedulers.io(), observeOnScheduler: Scheduler? = AndroidSchedulers.mainThread()) =
        this.subscribeOn(subscribeOnScheduler!!)
                .observeOn(observeOnScheduler)
                .subscribeWith(object : DisposableSubscriber<T>() {
                    override fun onError(t: Throwable?) {
                        onError?.let { it(t) }
                    }
                    
                    override fun onComplete() {
                        onComplete?.let { it() }
                    }
                    
                    override fun onNext(t: T) {
                        onNext?.let { it(t) }
                    }
                })

fun <T> T.mockSingleList(count: Int = 10): Single<List<T>> {
    val list = ArrayList<T>()
    for (index in 0..count) {
        list.add(this)
    }
    return Single.just(list)
}


//FIREBASE RX EXTENSION
//fun <T> DatabaseReference.toObservable(objType: Class<T>): Observable<T> {
//
//    val obs = Observable.create<T> { emitter ->
//
//        val listener = object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError?) {
//                emitter.onError(kotlin.Throwable(error?.message))
//                e("CONSI", "$error")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot?) {
//                try {
//                    snapshot?.getValue(objType)?.let {
//                        emitter.onNext(it)
//                    }
//                } catch (ex: Exception) {
//                    emitter.onError(kotlin.Exception("Parse error"))
//                    e("CONSI", "$snapshot")
//                }
//            }
//        }
//
//        this.addValueEventListener(listener)
//        emitter.setCancellable {
//            this.removeEventListener(listener)
//        }
//    }!!
//
//    obs.doOnDispose { }
//
//    return obs
//}

//FIREBASE RX EXTENSION
//fun <T> DatabaseReference.toObservable(objType: Class<T>): Observable<T> {
//
//    val obs = Observable.create<T> { emitter ->
//
//        val listener = object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError?) {
//                emitter.onError(kotlin.Throwable(error?.message))
//                e("CONSI", "$error")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot?) {
//                try {
//                    snapshot?.getValue(objType)?.let {
//                        emitter.onNext(it)
//                    }
//                } catch (ex: Exception) {
//                    emitter.onError(kotlin.Exception("Parse error"))
//                    e("CONSI", "$snapshot")
//                }
//            }
//        }
//
//        this.addValueEventListener(listener)
//        emitter.setCancellable {
//            this.removeEventListener(listener)
//        }
//    }!!
//
//    obs.doOnDispose { }
//
//    return obs
//}