package com.natife.example.mysocketchatapp.data.socket.helpers

import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class TcpSocket {

    private val tcpPort = 6666
    private var socket: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    val mIsSocketExist = MutableStateFlow(false)
    val isSocketExist: StateFlow<Boolean> = mIsSocketExist

    val mUserListFlow = MutableStateFlow(listOf<User>())
    val userListFlow: StateFlow<List<User>> = mUserListFlow

    val mUserFlow = MutableStateFlow(User("роиориориор", ""))
    val userFlow: StateFlow<User> = mUserFlow

    val mMessageFlow = MutableStateFlow(MessageDto(User("", ""), ""))
    val messageFlow: StateFlow<MessageDto> = mMessageFlow

    var id = ""

    private val myJob = Job()
    private val myScope = CoroutineScope(myJob + Dispatchers.IO)

    val getUserJob = Job()
    val getUserScope = CoroutineScope(getUserJob + Dispatchers.IO)

    fun connectSocket(ip: String): Socket? {
        myScope.launch {
            socket = Socket(ip, tcpPort)
            mIsSocketExist.emit(true)
        }
        return socket
    }

    private fun reader(): BufferedReader? {
        reader = BufferedReader(InputStreamReader(socket?.getInputStream()))
        return reader
    }

    private fun writer(): PrintWriter? {
        writer = PrintWriter(OutputStreamWriter(socket?.getOutputStream()))
        return writer
    }

    fun send(command: String) {
        try {
            val writer = writer()
            writer?.println(command)
            writer?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun read(): String? {
        return try {
            reader()?.readLine()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun closeSocketConnection() {
        reader()?.close()
        writer()?.close()
        socket?.close()
        reader = null
        writer = null
        socket = null
    }

}