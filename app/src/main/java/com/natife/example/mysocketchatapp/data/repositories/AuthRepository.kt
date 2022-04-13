package com.natife.example.mysocketchatapp.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.models.*
import kotlinx.coroutines.*

class AuthRepository(
    private val context: Context,
    val tcpSocket: TcpSocket
) {

    private val myJob = Job()
    private val myScope = CoroutineScope(myJob + Dispatchers.IO)

    private val sharedPrefName = "sharedPref"
    private val userIdKey = "user_id"
    private val gson by lazy { Gson() }

    private fun saveUserId(userId: String) {
        context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit {
            putString(userIdKey, userId)
        }
    }

    private fun sendPingCommand() {
        val pingDto = PingDto(
            context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
                .getString(userIdKey, "0")
                .toString()
        )
        val pingDtoPayload = gson.toJson(pingDto)
        val baseDtoForPing = gson.toJson(BaseDto(BaseDto.Action.PING, pingDtoPayload))
        tcpSocket.writer.println(baseDtoForPing)
        tcpSocket.writer.flush()
    }

    suspend fun readSocketCommand(
        name: String
    ) {
        while (true) {
            val response = tcpSocket.reader.readLine()
            val parsedResponse = gson.fromJson(response, BaseDto::class.java)
            if (response != null) {
                when (parsedResponse.action) {
                    BaseDto.Action.CONNECTED -> {
                        val connectedBaseDtoPayload =
                            gson.fromJson(response, BaseDto::class.java).payload
                        val userId = gson
                            .fromJson(connectedBaseDtoPayload, ConnectedDto::class.java)
                            .id

                        saveUserId(userId)

                        val connectDto = gson.toJson(ConnectDto(userId, name))
                        val baseConnectDto = BaseDto(BaseDto.Action.CONNECT, connectDto)
                        val connectMessageToSocket = gson.toJson(baseConnectDto)
                        tcpSocket.writer.println(connectMessageToSocket)

                        myScope.launch {
                            while (true) {
                                delay(10000)
                                sendPingCommand()
                            }
                        }

                        tcpSocket.mIsAuthFinished.emit(true)
                        tcpSocket.writer.flush()
                    }

                    BaseDto.Action.USERS_RECEIVED -> {
                        val existUserList =
                            gson.fromJson(parsedResponse.payload, UsersReceivedDto::class.java)
                        tcpSocket.mUserListFlow.emit(existUserList.users)
                    }

                    BaseDto.Action.NEW_MESSAGE -> {
                        val existNewMessage =
                            gson.fromJson(parsedResponse.payload, MessageDto::class.java)
                        tcpSocket.mMessageFlow.emit(existNewMessage)
                    }

                    else -> {
                    }
                }
            }
        }
    }

}