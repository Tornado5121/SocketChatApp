package com.natife.example.mysocketchatapp.ui.authScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.example.mysocketchatapp.data.repositories.authRepo.AuthRepository
import com.natife.example.mysocketchatapp.data.repositories.userProfileRepo.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val mIsTransitToUserList = MutableLiveData<Boolean>()
    val isTransitToUserList = mIsTransitToUserList

    private val mIsConnectionExists = MutableLiveData<Boolean>()
    val isConnectionExists: LiveData<Boolean> = mIsConnectionExists

    fun getName(): String {
        return userProfileRepository.getName()
    }

    fun loginUser(name: String) {
        userProfileRepository.saveName(name)
        authRepository.readSocketCommand(name, authRepository.ipFlow.value)
    }

    fun isAuthorised() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.isAuthFinished.collectLatest {
                mIsTransitToUserList.postValue(it)
            }
        }
    }

    fun getIp() {
        authRepository.myWorkScope.launch {
            authRepository.getIp()
        }
    }

    fun isConnectionExists() {
        authRepository.myWorkScope.launch(Dispatchers.IO) {
            authRepository.isSocketExist.collectLatest {
                mIsConnectionExists.postValue(it)
            }
        }
    }

}
