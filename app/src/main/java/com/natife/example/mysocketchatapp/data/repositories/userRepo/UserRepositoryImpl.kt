package com.natife.example.mysocketchatapp.data.repositories.userRepo

import com.google.gson.Gson
import com.example.domain.helpers.TcpSocket
import com.example.domain.models.BaseDto
import com.example.domain.models.GetUsersDto
import com.example.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow

class UserRepositoryImpl(
    private val tcpSocket: TcpSocket,

    ) : UserRepository {

    private val gson = Gson()
     override var userListFlow: MutableStateFlow<List<User>> = tcpSocket.mUserListFlow


    override fun sendGetUserListCommand(id: String) {
        val getUsersDto = gson.toJson(GetUsersDto(id))
        val baseUserListDto = gson
            .toJson(BaseDto(BaseDto.Action.GET_USERS, getUsersDto))
        tcpSocket.send(baseUserListDto)
    }

}
