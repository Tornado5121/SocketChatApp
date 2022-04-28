package com.natife.example.mysocketchatapp.data.socket.helpers

import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface TcpSocket {

    var mUserListFlow: MutableStateFlow<List<User>>
    var mMessageFlow: MutableStateFlow<MessageDto>
    var userListFlow: StateFlow<List<User>>
    var messageFlow: StateFlow<MessageDto>

    fun connectSocket(ip: String)
    fun send(command: String)
    fun read(): String?
    fun closeSocketConnection()

}