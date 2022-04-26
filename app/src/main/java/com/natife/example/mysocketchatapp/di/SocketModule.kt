package com.natife.example.mysocketchatapp.di

import com.natife.example.mysocketchatapp.data.repositories.AuthRepository
import com.natife.example.mysocketchatapp.data.repositories.ChatRepository
import com.natife.example.mysocketchatapp.data.repositories.UserRepository
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.helpers.UdpSocket
import com.natife.example.mysocketchatapp.ui.authScreen.AuthViewModel
import com.natife.example.mysocketchatapp.ui.userListScreen.UserListViewModel
import com.natife.example.mysocketchatapp.ui.chatScreen.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { AuthRepository(androidContext(), get(), get()) }
    single { ChatRepository(get()) }
    single { UserRepository(get()) }
    single { UdpSocket() }
    single { TcpSocket() }

}

//val dataModule = module {
//    factory<UserFetcher> { UserFetcherImpl(RetrofitClient.api) }
//    factory<UserDataRepository> (UserDBRepQualifier){ DataBaseRepository(UserDataBase.getInstance(androidContext()).userDao) }
//    single<UserDataRepository> { UserRepository(get(UserDBRepQualifier), get()) }
//}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { UserListViewModel(get()) }
    viewModel { ChatViewModel(get()) }
}

