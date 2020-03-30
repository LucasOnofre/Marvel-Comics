package com.onoffrice.marvel_comics.data.di

import com.onoffrice.marvel_comics.data.repositories.CharactersRepository
import com.onoffrice.marvel_comics.data.repositories.CharactersRepositoryImp
import com.onoffrice.marvel_comics.ui.characterDetail.CharacterDetailViewModel
import com.onoffrice.marvel_comics.ui.characters.CharactersViewModel
import com.onoffrice.marvel_comics.ui.mostExpensiveComic.MostExpensiveComicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinInjector {

    val charactersModule = module {

        viewModel { CharactersViewModel(get()) }
        single<CharactersRepository> { CharactersRepositoryImp() }
    }

    val charactersDetailModule = module {
        viewModel { CharacterDetailViewModel(get()) }
    }

    val mostExpensiveComicModule = module {
        viewModel { MostExpensiveComicViewModel() }
    }
}