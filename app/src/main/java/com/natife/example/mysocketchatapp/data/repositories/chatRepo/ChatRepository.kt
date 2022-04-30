package com.natife.example.mysocketchatapp.data.repositories.chatRepo

import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import kotlinx.coroutines.flow.MutableStateFlow

interface ChatRepository {

    val messageFlow: MutableStateFlow<MessageDto>

    fun sendMessage(receiver: String, message: String, id: String)

}