package com.example.domain.helpers

import com.example.domain.models.MessageDto
import com.example.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

const val tcpPort = 6666

class TcpSocketImpl : TcpSocket {

    private var socket: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    override var mUserListFlow = MutableStateFlow(listOf<User>())
    override var userListFlow: StateFlow<List<User>> = mUserListFlow

    override var mMessageFlow = MutableStateFlow(MessageDto(User("", ""), ""))
    override var messageFlow: StateFlow<MessageDto> = mMessageFlow

    override suspend fun connectSocket(ip: String) {
        socket = Socket(ip, tcpPort)
        reader = BufferedReader(InputStreamReader(socket?.getInputStream()))
        writer = PrintWriter(OutputStreamWriter(socket?.getOutputStream()))
    }

    override fun isSocketConnected(): Boolean? {
        return socket?.isConnected
    }

    override fun send(command: String) {
        try {
            writer?.println(command)
            writer?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun read(): String? {
        return try {
            reader?.readLine()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override fun closeSocketConnection() {
        reader?.close()
        writer?.close()
        socket?.close()
        reader = null
        writer = null
        socket = null
    }

}