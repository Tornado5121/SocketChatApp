package com.natife.example.mysocketchatapp.ui.userListScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.example.mysocketchatapp.data.repositories.UserRepository
import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class UserListViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val mLiveData = MutableLiveData<List<User>>()
    val liveData = mLiveData

    private val myScope = CoroutineScope(Job() + Dispatchers.IO)

    fun sendGetUserCommand() {
        myScope.launch {
            while (true) {
                userRepository.sendGetUserListCommand()
                delay(3000)
            }
        }
    }

    fun getUserList() {
        myScope.launch {
            userRepository.tcpSocket.userListFlow.collectLatest {
                mLiveData.postValue(it + User("fds", "dfdsf"))
            }
        }
    }

}