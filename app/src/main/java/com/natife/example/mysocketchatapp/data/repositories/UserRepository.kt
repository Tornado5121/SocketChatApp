package com.natife.example.mysocketchatapp.data.repositories

import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.models.BaseDto
import com.natife.example.mysocketchatapp.data.socket.models.GetUsersDto
import kotlinx.coroutines.*

class UserRepository(
    val tcpSocket: TcpSocket
) {

    private val gson = Gson()

    fun sendGetUserListCommand() {
        tcpSocket.getUserScope.launch {
            while (true) {
                val getUsersDto = gson.toJson(GetUsersDto(tcpSocket.id))
                val baseUserListDto = gson
                    .toJson(BaseDto(BaseDto.Action.GET_USERS, getUsersDto))
                tcpSocket.send(baseUserListDto)
                delay(3000)
            }
        }
    }

}
