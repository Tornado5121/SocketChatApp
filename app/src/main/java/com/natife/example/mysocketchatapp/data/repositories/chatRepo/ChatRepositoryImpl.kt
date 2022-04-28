package com.natife.example.mysocketchatapp.data.repositories.chatRepo

import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.models.BaseDto
import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import com.natife.example.mysocketchatapp.data.socket.models.SendMessageDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class ChatRepositoryImpl(
    private val tcpSocket: TcpSocket
) : ChatRepository {

    private val gson = Gson()

    private val mMessageFlow = MutableStateFlow(MessageDto(User("", ""), ""))
    override val messageFlow: StateFlow<MessageDto> = mMessageFlow

    override fun sendMessage(receiver: String, message: String, id: String) {
        val sendMessageDto = gson.toJson(
            SendMessageDto(
                id,
                receiver,
                message
            )
        )
        val baseUserListDto = gson.toJson(BaseDto(BaseDto.Action.SEND_MESSAGE, sendMessageDto))
        tcpSocket.send(baseUserListDto)
    }

    override suspend fun getNewMessage() {
        tcpSocket.messageFlow.collectLatest {
            mMessageFlow.emit(it)
        }
    }

}

