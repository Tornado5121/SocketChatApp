package com.natife.example.mysocketchatapp.data.socket.helpers

import android.content.Context
import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class TcpSocket(
    private val context: Context,
    private val udpSocket: UdpSocket
) {
    val mIsAuthFinished = MutableStateFlow(false)
    var isAuthFinished: StateFlow<Boolean> = mIsAuthFinished

    val mUserListFlow = MutableStateFlow(listOf<User>())
    var userListFlow: StateFlow<List<User>> = mUserListFlow

    val mUserFlow = MutableStateFlow(User("роиориориор", ""))
    var userFlow: StateFlow<User> = mUserFlow

    val mMessageFlow = MutableStateFlow(MessageDto(User("", ""), ""))
    var messageFlow: StateFlow<MessageDto> = mMessageFlow

    private val tcpPort = 6666
    private val sharedPrefName = "sharedPref"
    private val userIdKey = "user_id"

    val socket by lazy { Socket(udpSocket.getIpAddress(), tcpPort) }
    val reader by lazy { BufferedReader(InputStreamReader(socket.getInputStream())) }
    val writer by lazy { PrintWriter(OutputStreamWriter(socket.getOutputStream())) }

    val id by lazy {
        context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
            .getString(userIdKey, "1")
            .toString()
    }

}