package com.natife.example.mysocketchatapp.data.repositories.userRepo

import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {

    val userListFlow: StateFlow<List<User>>

    fun sendGetUserListCommand(id: String)
    suspend fun getUser()

}