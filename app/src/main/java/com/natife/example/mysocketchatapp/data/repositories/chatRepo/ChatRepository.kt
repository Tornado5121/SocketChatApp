package com.natife.example.mysocketchatapp.data.repositories.chatRepo

import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {

    val messageFlow: StateFlow<MessageDto>

    fun sendMessage(receiver: String, message: String, id: String)
    suspend fun getNewMessage()

}