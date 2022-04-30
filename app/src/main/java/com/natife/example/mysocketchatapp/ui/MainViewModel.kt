package com.natife.example.mysocketchatapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.example.mysocketchatapp.data.repositories.authRepo.AuthRepository
import com.natife.example.mysocketchatapp.data.repositories.userProfileRepo.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val mIsConnectionExists = MutableLiveData<Boolean>()
    val isConnectionExists: LiveData<Boolean> = mIsConnectionExists

    private var isFirstLaunch = true

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.isSocketExist.collectLatest {
                mIsConnectionExists.postValue(it)
                if (!it) {
                    if (!isFirstLaunch) {
                        clearNameFromPrefsAfterLostConnection()
                        isFirstLaunch = false
                    }
                }
            }
        }
    }

    private fun clearNameFromPrefsAfterLostConnection() {
        userProfileRepository.clearName()
    }

}