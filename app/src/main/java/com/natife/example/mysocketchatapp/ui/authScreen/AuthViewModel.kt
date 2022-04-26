package com.natife.example.mysocketchatapp.ui.authScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.example.mysocketchatapp.data.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val mIsUserAuth = MutableLiveData<Boolean>()
    val isUserAuth = mIsUserAuth

    private val mIsSocketExist = MutableLiveData<Boolean>()
    val isSocketExist: LiveData<Boolean> = mIsSocketExist

    private val mIsIpExists = MutableLiveData<Boolean>()
    val isIpExists = mIsIpExists

    private val mIsAuthLoggingOn = MutableLiveData<Boolean>()
    val isAuthLoggingOn = mIsAuthLoggingOn

    fun turnOnAutoLog() {
        mIsAuthLoggingOn.postValue(true)
    }

    fun turnOffAutoLog() {
        mIsAuthLoggingOn.postValue(false)
    }

    fun launchReadSocketCommand(name: String) {
        authRepository.myReadScope.launch {
            authRepository.tcpSocket.mIsSocketExist.emit(true)
            authRepository.ipFlow.collectLatest {
                authRepository.readSocketCommand(name, it)
            }
            mIsAuthLoggingOn.postValue(false)
        }
    }

    fun isAuthorised() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.isAuthFinished.collectLatest {
                mIsUserAuth.postValue(it)
            }
        }
    }

    fun getIp() {
        authRepository.myDefaultScope.launch {
            authRepository.getIp()
        }
    }

    fun isIpExist() {
        authRepository.myDefaultScope.launch {
            authRepository.ipFlow.collectLatest {
                if (it.isNotEmpty())
                    isIpExists.postValue(true)
            }
        }
    }

    fun isSocketExist() {
        authRepository.myDefaultScope.launch(Dispatchers.IO) {
            authRepository.tcpSocket.isSocketExist.collectLatest {
                mIsSocketExist.postValue(it)
            }
        }
    }

}
