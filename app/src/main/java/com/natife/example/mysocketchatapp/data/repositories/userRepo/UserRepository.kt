package com.natife.example.mysocketchatapp.data.repositories.userRepo

import com.example.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow

interface UserRepository {

    val userListFlow: MutableStateFlow<List<User>>
    fun sendGetUserListCommand(id: String)

}