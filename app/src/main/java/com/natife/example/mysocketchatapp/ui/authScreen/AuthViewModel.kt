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

    private val mLiveData = MutableLiveData<Boolean>()
    val liveData = mLiveData

    private val myScope = CoroutineScope(Job() + Dispatchers.IO)

    fun launchReadSocketCommand(name: String) {
        myScope.launch {
            authRepository.readSocketCommand(name)
        }
    }

    fun isAuthorised() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.tcpSocket.isAuthFinished.collectLatest {
                mLiveData.postValue(it)
            }
        }
    }
}
