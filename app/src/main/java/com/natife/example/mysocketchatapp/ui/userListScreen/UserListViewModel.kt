package com.natife.example.mysocketchatapp.ui.userListScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.example.mysocketchatapp.data.repositories.authRepo.AuthRepository
import com.natife.example.mysocketchatapp.data.repositories.userProfileRepo.UserProfileRepository
import com.natife.example.mysocketchatapp.data.repositories.userRepo.UserRepository
import com.example.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val sendGetUserCommandDelay = 2000L

class UserListViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val mUserListLiveData = MutableLiveData<List<User>>()
    val userListLiveData = mUserListLiveData
    private var sendGetUsersCommandJob: Job

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.userListFlow.collectLatest {
                mUserListLiveData.postValue(it)
            }
        }

        sendGetUsersCommandJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                userRepository.sendGetUserListCommand(authRepository.getMyId())
                delay(sendGetUserCommandDelay)
            }
        }

    }

    fun logOut() {
        viewModelScope.launch {
            userProfileRepository.clearName()
            authRepository.closeSocket()
            sendGetUsersCommandJob.cancel()
            sendGetUsersCommandJob.join()
        }
    }

}





