package com.natife.example.mysocketchatapp.di

import android.content.Context
import com.natife.example.mysocketchatapp.data.repositories.authRepo.AuthRepository
import com.natife.example.mysocketchatapp.data.repositories.authRepo.AuthRepositoryImpl
import com.natife.example.mysocketchatapp.data.repositories.chatRepo.ChatRepository
import com.natife.example.mysocketchatapp.data.repositories.chatRepo.ChatRepositoryImpl
import com.natife.example.mysocketchatapp.data.repositories.userProfileRepo.UserProfileRepository
import com.natife.example.mysocketchatapp.data.repositories.userProfileRepo.UserProfileRepositoryImpl
import com.natife.example.mysocketchatapp.data.repositories.userRepo.UserRepository
import com.natife.example.mysocketchatapp.data.repositories.userRepo.UserRepositoryImpl
import com.example.domain.helpers.TcpSocket
import com.example.domain.helpers.TcpSocketImpl
import com.example.domain.helpers.UdpSocket
import com.example.domain.helpers.UdpSocketImpl
import com.natife.example.mysocketchatapp.ui.MainViewModel
import com.natife.example.mysocketchatapp.ui.authScreen.AuthViewModel
import com.natife.example.mysocketchatapp.ui.chatScreen.ChatViewModel
import com.natife.example.mysocketchatapp.ui.userListScreen.UserListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val SHARED_PREF_NAME = "resources"
val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserProfileRepository> { UserProfileRepositoryImpl(get()) }
    single<UdpSocket> { UdpSocketImpl() }
    single<TcpSocket> { TcpSocketImpl() }
    single { androidContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { UserListViewModel(get(), get(), get()) }
    viewModel { (id: String) -> ChatViewModel(get(), get(), get(), id) }
    viewModel { MainViewModel(get(), get()) }
}

