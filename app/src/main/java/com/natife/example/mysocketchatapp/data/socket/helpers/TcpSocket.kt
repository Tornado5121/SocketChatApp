package com.natife.example.mysocketchatapp.data.socket.helpers

import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface TcpSocket {

    val mUserListFlow: MutableStateFlow<List<User>>
    val mMessageFlow: MutableStateFlow<MessageDto>
    val userListFlow: StateFlow<List<User>>
    val messageFlow: StateFlow<MessageDto>

    fun isSocketConnected(): Boolean?
    suspend fun connectSocket(ip: String)
    fun send(command: String)
    fun read(): String?
    fun closeSocketConnection()

}