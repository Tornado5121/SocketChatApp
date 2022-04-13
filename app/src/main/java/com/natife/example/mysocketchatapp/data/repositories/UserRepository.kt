package com.natife.example.mysocketchatapp.data.repositories

import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.models.BaseDto
import com.natife.example.mysocketchatapp.data.socket.models.GetUsersDto

class UserRepository(
    val tcpSocket: TcpSocket
) {
    private val gson = Gson()

    fun sendGetUserListCommand() {
        val getUsersDto = gson.toJson(GetUsersDto(tcpSocket.id))
        val baseUserListDto = gson.toJson(BaseDto(BaseDto.Action.GET_USERS, getUsersDto))
        tcpSocket.writer.println(baseUserListDto)
        tcpSocket.writer.flush()
    }

}
