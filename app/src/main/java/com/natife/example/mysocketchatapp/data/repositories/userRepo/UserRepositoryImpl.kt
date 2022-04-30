package com.natife.example.mysocketchatapp.data.repositories.userRepo

import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.models.BaseDto
import com.natife.example.mysocketchatapp.data.socket.models.GetUsersDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
