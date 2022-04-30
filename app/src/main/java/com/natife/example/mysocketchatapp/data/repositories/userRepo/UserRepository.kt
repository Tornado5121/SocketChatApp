package com.natife.example.mysocketchatapp.data.repositories.userRepo

import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.flow.MutableStateFlow

interface UserRepository {

    val userListFlow: MutableStateFlow<List<User>>
    fun sendGetUserListCommand(id: String)

}