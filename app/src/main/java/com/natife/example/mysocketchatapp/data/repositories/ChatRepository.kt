package com.natife.example.mysocketchatapp.data.repositories

import android.util.Log.d
import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.models.BaseDto
import com.natife.example.mysocketchatapp.data.socket.models.SendMessageDto
import kotlinx.coroutines.flow.collect

class ChatRepository(
    val tcpSocket: TcpSocket
) {

    private val gson = Gson()

    fun sendMessage(receiver: String, message: String) {
        val sendMessageDto = gson.toJson(
            SendMessageDto(
                tcpSocket.id,
                receiver,
                message
            )
        )
        val baseUserListDto = gson.toJson(BaseDto(BaseDto.Action.SEND_MESSAGE, sendMessageDto))
        tcpSocket.send(baseUserListDto)
    }

    suspend fun getUserById(id: String) {
        tcpSocket.userListFlow.collect { userList ->
            d("userList", userList.toString())
            userList.forEach {
                if (it.id == id) {
                    tcpSocket.mUserFlow.emit(it)
                }
            }
        }
    }

}

