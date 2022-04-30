package com.natife.example.mysocketchatapp.data.repositories.authRepo

import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.helpers.UdpSocket
import com.natife.example.mysocketchatapp.data.socket.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.SocketException

class AuthRepositoryImpl(
    private val tcpSocket: TcpSocket,
    private val udpSocket: UdpSocket
) : AuthRepository {

    private var id = ""

    private val mIsSocketExist = MutableStateFlow(false)
    override val isSocketExist: StateFlow<Boolean> = mIsSocketExist

    private val myWorkJob by lazy { Job() }
    override val myWorkScope by lazy { CoroutineScope(myWorkJob + Dispatchers.IO) }
    private lateinit var readSocketCommandJob: Job
    private lateinit var cancelJobIfNotGettingPong: Job
    private lateinit var sendPingCommandJob: Job

    private val mIpFlow = MutableStateFlow("")
    override val ipFlow: MutableStateFlow<String> = mIpFlow

    private val gson by lazy { Gson() }

    override fun getMyId(): String {
        return id
    }

    private fun launchPingPongMechanism(isSocketConnected: Boolean) {
        sendPingCommandJob = myWorkScope.launch {
            while (isSocketConnected) {
                delay(10000)
                val pingDto = PingDto(id)
                val pingDtoPayload = gson.toJson(pingDto)
                val baseDtoForPing = gson.toJson(BaseDto(BaseDto.Action.PING, pingDtoPayload))
                tcpSocket.send(baseDtoForPing)

                cancelJobIfNotGettingPong = myWorkScope.launch {
                    delay(15000)
                    try {
                        closeSocket()
                    } catch (e: SocketException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private suspend fun connectSocketAndHelperObjects() {
        val ip = udpSocket.getIpAddress()
        tcpSocket.connectSocket(ip)
    }

    override fun sendConnectCommand(userId: String, name: String) {
        val connectDto = gson.toJson(ConnectDto(userId, name))
        val baseConnectDto = BaseDto(BaseDto.Action.CONNECT, connectDto)
        val connectMessageToSocket = gson.toJson(baseConnectDto)
        tcpSocket.send(connectMessageToSocket)
    }

    override suspend fun closeSocket() {
        mIsSocketExist.value = false
        tcpSocket.closeSocketConnection()
        readSocketCommandJob.cancel()
        readSocketCommandJob.join()
        sendPingCommandJob.cancel()
        sendPingCommandJob.join()
    }

    override fun readSocketCommand(
        name: String
    ) {
        readSocketCommandJob = myWorkScope.launch {
            connectSocketAndHelperObjects()
            mIsSocketExist.emit(true)
            while (true) {
                try {
                    val response = tcpSocket.read()
                    if (!response.isNullOrEmpty()) {
                        val parsedResponse = gson.fromJson(response, BaseDto::class.java)
                        when (parsedResponse.action) {
                            BaseDto.Action.CONNECTED -> {
                                val userId = gson
                                    .fromJson(
                                        parsedResponse.payload,
                                        ConnectedDto::class.java
                                    )
                                    .id

                                id = userId
                                sendConnectCommand(userId, name)
                                tcpSocket.isSocketConnected()?.let { launchPingPongMechanism(it) }
                                mIsSocketExist.emit(true)
                            }

                            BaseDto.Action.PONG -> {
                                cancelJobIfNotGettingPong.cancel()
                                cancelJobIfNotGettingPong.join()
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
                                tcpSocket.mMessageFlow.value = existNewMessage
                            }

                            else -> {
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