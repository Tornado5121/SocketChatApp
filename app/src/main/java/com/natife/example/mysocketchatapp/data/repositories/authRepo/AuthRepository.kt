package com.natife.example.mysocketchatapp.data.repositories.authRepo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    val isSocketExist: StateFlow<Boolean>
    val ipFlow: MutableStateFlow<String>
    val myWorkScope: CoroutineScope

    fun getMyId(): String
    fun sendConnectCommand(userId: String, name: String)
    suspend fun closeSocket()
    fun readSocketCommand(name: String)

}