package com.itskidan.kinostock.domain

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

// Inherit from LifecycleObserver
class AutoDisposable : LifecycleObserver {
    // Use CompositeDisposable to disable all Observables
    lateinit var compositeDisposable: CompositeDisposable
    // Here we pass a link to the life cycle of the component that will be monitored
    fun bindTo(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        compositeDisposable = CompositeDisposable()
    }
    // Method for adding Observable to CompositeDisposable
    fun add(disposable: Disposable) {
        if (::compositeDisposable.isInitialized) {
            compositeDisposable.add(disposable)
        } else {
            throw NotImplementedError("must bind AutoDisposable to a Lifecycle first")
        }
    }
    // This annotation allows you to call a method on a lifecycle event
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        compositeDisposable.dispose()
    }
}
// Extension
fun Disposable.addTo(autoDisposable: AutoDisposable) {
    autoDisposable.add(this)
}