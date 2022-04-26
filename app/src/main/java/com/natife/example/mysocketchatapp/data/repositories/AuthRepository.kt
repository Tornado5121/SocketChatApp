package com.natife.example.mysocketchatapp.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.helpers.UdpSocket
import com.natife.example.mysocketchatapp.data.socket.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.SocketException

class AuthRepository(
    private val context: Context,
    val tcpSocket: TcpSocket,
    private val udpSocket: UdpSocket
) {

    private val sharePrefName = "resources"
    private val userNameKey = "myName"

    private val mIsAuthFinished = MutableStateFlow(false)
    var isAuthFinished: StateFlow<Boolean> = mIsAuthFinished

    private val myReadJob by lazy { Job() }
    val myReadScope by lazy { CoroutineScope(myReadJob + Dispatchers.IO) }

    private val myDefaultJob by lazy { Job() }
    val myDefaultScope by lazy { CoroutineScope(myDefaultJob + Dispatchers.IO) }

    private val lostSocketJob by lazy { Job() }
    private val lostSocketScope by lazy { CoroutineScope(lostSocketJob + Dispatchers.IO) }

    private val mIpFlow = MutableStateFlow("")
    val ipFlow = mIpFlow

    private val gson by lazy { Gson() }

    private lateinit var ip: String

    fun getIp() {
        myDefaultScope.launch {
            ip = udpSocket.ipAddress()
            mIpFlow.emit(ip)
        }
    }

    private fun sendPingCommand() {
        val pingDto = PingDto(
            tcpSocket.id
        )
        val pingDtoPayload = gson.toJson(pingDto)
        val baseDtoForPing = gson.toJson(BaseDto(BaseDto.Action.PING, pingDtoPayload))
        tcpSocket.send(baseDtoForPing)
    }

    private fun saveName(name: String) {
        context.getSharedPreferences(sharePrefName, Context.MODE_PRIVATE).edit {
            putString(userNameKey, name)
        }
    }

    fun readSocketCommand(
        name: String,
        ip: String
    ) {
        tcpSocket.connectSocket(ip)
        myReadScope.launch {
            while (true) {
                try {
                    saveName(name)
                    val response = tcpSocket.read()
                    if (response != null) {
                        if (response.isNotEmpty()) {
                            val parsedResponse = gson.fromJson(response, BaseDto::class.java)
                            when (parsedResponse.action) {
                                BaseDto.Action.CONNECTED -> {
                                    val connectedBaseDtoPayload =
                                        gson.fromJson(response, BaseDto::class.java).payload
                                    val userId = gson
                                        .fromJson(connectedBaseDtoPayload, ConnectedDto::class.java)
                                        .id

                                    tcpSocket.id = userId

                                    val connectDto = gson.toJson(ConnectDto(userId, name))
                                    val baseConnectDto = BaseDto(BaseDto.Action.CONNECT, connectDto)
                                    val connectMessageToSocket = gson.toJson(baseConnectDto)
                                    tcpSocket.send(connectMessageToSocket)

                                    myDefaultScope.launch {
                                        while (true) {
                                            delay(6000)
                                            sendPingCommand()
                                        }
                                    }

                                    lostSocketScope.launch {
                                        while (true) {
                                            delay(11000)
                                            try {
                                                tcpSocket.mIsSocketExist.emit(false)
                                                tcpSocket.closeSocketConnection()
                                                myDefaultJob.cancel()
                                                tcpSocket.getUserJob.cancel()
                                                myReadJob.cancel()
                                            } catch (e: SocketException) {
                                                e.printStackTrace()
                                            }
                                        }
                                    }

                                    mIsAuthFinished.emit(true)
                                }

                                BaseDto.Action.PONG -> {
                                    lostSocketJob.cancel()
                                }

                                BaseDto.Action.USERS_RECEIVED -> {
                                    val existUserList =
                                        gson.fromJson(
                                            parsedResponse.payload,
                                            UsersReceivedDto::class.java
                                        )
                                    tcpSocket.mUserListFlow.emit(existUserList.users)
                                }

                                BaseDto.Action.NEW_MESSAGE -> {
                                    val existNewMessage =
                                        gson.fromJson(
                                            parsedResponse.payload,
                                            MessageDto::class.java
                                        )
                                    tcpSocket.mMessageFlow.emit(existNewMessage)
                                }

                                else -> {
                                }
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }
            }
        }
    }

}