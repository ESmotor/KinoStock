package com.itskidan.kinostock.di

import com.itskidan.kinostock.di.modules.DatabaseModule
import com.itskidan.kinostock.di.modules.DomainModule
import com.itskidan.kinostock.di.modules.RemoteModule
import com.itskidan.kinostock.domain.TmdbApi
import com.itskidan.kinostock.view.fragments.DetailFragment
import com.itskidan.kinostock.viewmodel.MainFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RemoteModule::class,
        DomainModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {
    // method for injecting dependencies into the HomeFragmentViewModel
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(detailFragment: DetailFragment)
}