package com.natife.example.mysocketchatapp.data.repositories.chatRepo

import com.google.gson.Gson
import com.example.domain.helpers.TcpSocket
import com.example.domain.models.BaseDto
import com.example.domain.models.MessageDto
import com.example.domain.models.SendMessageDto
import kotlinx.coroutines.flow.MutableStateFlow

class ChatRepositoryImpl(
    private val tcpSocket: TcpSocket
) : ChatRepository {

    private val gson = Gson()

    override val messageFlow: MutableStateFlow<MessageDto> = tcpSocket.mMessageFlow

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

}

