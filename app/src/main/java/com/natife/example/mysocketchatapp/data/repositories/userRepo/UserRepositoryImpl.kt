package com.natife.example.mysocketchatapp.data.repositories.userRepo

import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.models.BaseDto
import com.natife.example.mysocketchatapp.data.socket.models.GetUsersDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class UserRepositoryImpl(
    private val tcpSocket: TcpSocket
) : UserRepository {

    private val gson = Gson()

    private val mUserListFlow = MutableStateFlow(listOf<User>())
    override val userListFlow: StateFlow<List<User>> = mUserListFlow

    override fun sendGetUserListCommand(id: String) {
        val getUsersDto = gson.toJson(GetUsersDto(id))
        val baseUserListDto = gson
            .toJson(BaseDto(BaseDto.Action.GET_USERS, getUsersDto))
        tcpSocket.send(baseUserListDto)
    }

    override suspend fun getUser() {
        tcpSocket.userListFlow.collectLatest {
            mUserListFlow.emit(it)
        }
    }

}
