package com.natife.example.mysocketchatapp.ui.authScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.example.mysocketchatapp.data.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val mAuthLiveData = MutableLiveData<Boolean>()
    val authLiveData = mAuthLiveData

    private val myScope = CoroutineScope(Job() + Dispatchers.IO)

    fun launchReadSocketCommand(name: String) {
        myScope.launch {
            authRepository.readSocketCommand(name)
        }
    }

    fun isAuthorised() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.tcpSocket.isAuthFinished.collectLatest {
                mAuthLiveData.postValue(it)
            }
        }
    }



}
