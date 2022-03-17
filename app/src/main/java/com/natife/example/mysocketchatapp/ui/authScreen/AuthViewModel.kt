package com.natife.example.mysocketchatapp.ui.authScreen

import androidx.lifecycle.ViewModel
import com.natife.example.mysocketchatapp.data.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getAuthorised(userName: String) {

        authRepository.getAuthToken(userName)
    }

    fun launchCheckSocketConnection() {

        while (true) {
            authRepository.sendPingCommand()
        }
    }

}