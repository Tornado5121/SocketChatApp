package com.natife.example.mysocketchatapp.data.repositories.authRepo

import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.helpers.UdpSocket
import com.natife.example.mysocketchatapp.data.socket.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.SocketException

const val SHARED_PREF_NAME = "resources"
const val USER_NAME_KEY = "myName"

class AuthRepositoryImpl(
    private val tcpSocket: TcpSocket,
    private val udpSocket: UdpSocket
) : AuthRepository {

    override var id = ""

    private val mIsSocketExist = MutableStateFlow(false)
    override val isSocketExist: StateFlow<Boolean> = mIsSocketExist

    private var mIsAuthFinished = MutableStateFlow(false)
    override val isAuthFinished: StateFlow<Boolean> = mIsAuthFinished

    private val myWorkJob by lazy { Job() }
    override val myWorkScope by lazy { CoroutineScope(myWorkJob + Dispatchers.IO) }
    private lateinit var readSocketJobFromBuilder: Job

    private val myCancelJob by lazy { Job() }
    private val myCancelScope by lazy { CoroutineScope(myCancelJob + Dispatchers.IO) }
    private lateinit var cancelJobForNotGettingPong: Job

    private val mIpFlow = MutableStateFlow("")
    override val ipFlow: MutableStateFlow<String> = mIpFlow

    private val gson by lazy { Gson() }

    override fun getIp() {
        myWorkScope.launch {
            mIpFlow.emit(udpSocket.ipAddress())
        }
    }

    override fun launchPingPongMechanism() {
        myWorkScope.launch {
            while (true) {
                delay(3000)
                val pingDto = PingDto(id)
                val pingDtoPayload = gson.toJson(pingDto)
                val baseDtoForPing = gson.toJson(BaseDto(BaseDto.Action.PING, pingDtoPayload))
                tcpSocket.send(baseDtoForPing)
            }
        }
    }

    override fun sendConnectCommand(userId: String, name: String) {
        val connectDto = gson.toJson(ConnectDto(userId, name))
        val baseConnectDto = BaseDto(BaseDto.Action.CONNECT, connectDto)
        val connectMessageToSocket = gson.toJson(baseConnectDto)
        tcpSocket.send(connectMessageToSocket)
    }

    override suspend fun closeSocket() {
        mIsSocketExist.emit(false)
        mIsAuthFinished.emit(false)
        tcpSocket.closeSocketConnection()
        readSocketJobFromBuilder.cancel()
        readSocketJobFromBuilder.join()
    }

    override fun disconnectSocket() {
        cancelJobForNotGettingPong = myCancelScope.launch {
            while (true) {
                delay(4000)
                try {
                    closeSocket()
                } catch (e: SocketException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun readSocketCommand(
        name: String,
        ip: String
    ) {
        readSocketJobFromBuilder = myWorkScope.launch {
            tcpSocket.connectSocket(ip)
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
                                launchPingPongMechanism()
                                disconnectSocket()
                                mIsAuthFinished.emit(true)
                            }

                            BaseDto.Action.PONG -> {
                                cancelJobForNotGettingPong.cancel()
                            }

                            BaseDto.Action.USERS_RECEIVED -> {
                                val existUserList =
                                    gson.fromJson(
                                        parsedResponse.payload,
                                        UsersReceivedDto::class.java
                                    )
                                tcpSocket.mUserListFlow.value = existUserList.users
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