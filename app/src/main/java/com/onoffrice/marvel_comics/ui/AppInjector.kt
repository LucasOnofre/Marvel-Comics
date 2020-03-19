package com.onoffrice.marvel_comics.ui

import com.onoffrice.marvel_comics.data.repositories.CharactersRepository
import com.onoffrice.marvel_comics.ui.characterDetail.CharacterDetailViewModel
import com.onoffrice.marvel_comics.ui.characters.CharactersViewModel
import com.onoffrice.marvel_comics.ui.mostExpensiveComic.MostExpensiveComicViewModel


object AppInjector {

    //VIEW MODELS
    fun getCharacterViewModelFactory() = CharactersViewModel.Factory(CharactersRepository)

    fun getCharacterDetailViewModelFactory() = CharacterDetailViewModel.Factory(CharactersRepository)

    fun getMostExpensiveComicViewModelFactory() = MostExpensiveComicViewModel.Factory()


//    //REPOSITORIES
//    private object Repository {
//
//        fun getAuthRepository() = AuthRepositoryImpl(DataSource.getAuthRemoteDataSource())
//    }
//
//    private object DataSource {
//
//        fun getAuthRemoteDataSource() = AuthRemoteDataSource(ServiceType(AuthService::class.java, NetworkConstants.AUTH_URL))
//
//        fun getJsonPlaceholderDataSurce() = JsonPlaceholderRemoteDataSource(ServiceType(JsonPlaceholderService::class.java, "https://jsonplaceholder.typicode.com/"))
//
//        fun getEstablishmentDataSource() = EstablishmentRemoteDataSource(ServiceType(EstablishmentService::class.java, NetworkConstants.BASE_URL))
//
//        fun getContactDataSource() = ContactLocalDataSource()
//
//        fun getFaqDataSource() = FaqRemoteDataSource(ServiceType(FaqService::class.java, NetworkConstants.BASE_URL))
//
//        fun getUsersDataSource() = UsersRemoteDataSource(ServiceType(UsersService::class.java, NetworkConstants.USERS_URL))
//
//        fun getBannersDataSource() = UsersRemoteDataSource(ServiceType(UsersService::class.java, NetworkConstants.BASE_URL))
//    }

}