package com.natife.example.mysocketchatapp.di

import com.natife.example.mysocketchatapp.data.repositories.authRepo.AuthRepository
import com.natife.example.mysocketchatapp.data.repositories.authRepo.AuthRepositoryImpl
import com.natife.example.mysocketchatapp.data.repositories.chatRepo.ChatRepository
import com.natife.example.mysocketchatapp.data.repositories.chatRepo.ChatRepositoryImpl
import com.natife.example.mysocketchatapp.data.repositories.userProfileRepo.UserProfileRepository
import com.natife.example.mysocketchatapp.data.repositories.userProfileRepo.UserProfileRepositoryImpl
import com.natife.example.mysocketchatapp.data.repositories.userRepo.UserRepository
import com.natife.example.mysocketchatapp.data.repositories.userRepo.UserRepositoryImpl
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocketImpl
import com.natife.example.mysocketchatapp.data.socket.helpers.UdpSocket
import com.natife.example.mysocketchatapp.data.socket.helpers.UdpSocketImpl
import com.natife.example.mysocketchatapp.ui.authScreen.AuthViewModel
import com.natife.example.mysocketchatapp.ui.chatScreen.ChatViewModel
import com.natife.example.mysocketchatapp.ui.userListScreen.UserListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserProfileRepository> { UserProfileRepositoryImpl(androidContext()) }
    single<UdpSocket> { UdpSocketImpl() }
    single<TcpSocket> { TcpSocketImpl() }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { UserListViewModel(get(), get(), get()) }
    viewModel { (id: String) -> ChatViewModel(get(), get(), get(), id) }
}

