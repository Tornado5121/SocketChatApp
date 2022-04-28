package com.natife.example.mysocketchatapp.data.repositories.authRepo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    var id: String
    val isSocketExist: StateFlow<Boolean>
    val isAuthFinished: StateFlow<Boolean>
    val ipFlow: MutableStateFlow<String>
    val myWorkScope: CoroutineScope

    fun getIp()
    fun launchPingPongMechanism()
    fun sendConnectCommand(userId: String, name: String)
    suspend fun closeSocket()
    fun disconnectSocket()
    fun readSocketCommand(name: String, ip: String)

}