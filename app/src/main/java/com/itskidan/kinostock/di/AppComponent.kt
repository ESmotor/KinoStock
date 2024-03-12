package com.itskidan.kinostock.di

import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.di.modules.DatabaseModule
import com.itskidan.kinostock.di.modules.DomainModule
import com.itskidan.remote_module.RemoteModule
import com.itskidan.kinostock.view.fragments.DetailFragment
import com.itskidan.kinostock.view.fragments.FavoriteFragment
import com.itskidan.kinostock.view.fragments.MainFragment
import com.itskidan.kinostock.viewmodel.FavoriteFragmentViewModel
import com.itskidan.kinostock.viewmodel.MainFragmentViewModel
import com.itskidan.remote_module.RemoteProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [RemoteProvider::class],
    modules = [
        DomainModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {
    // method for injecting dependencies into the HomeFragmentViewModel
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(detailFragment: DetailFragment)
    fun inject(mainFragment: MainFragment)
    fun inject(favoriteFragment: FavoriteFragment)
    fun inject(favoriteFragmentViewModel: FavoriteFragmentViewModel)
    fun inject(mainRepository: MainRepository)
}