package com.natife.example.mysocketchatapp.data

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.BaseDto
import com.natife.example.mysocketchatapp.data.socket.ConnectDto
import com.natife.example.mysocketchatapp.data.socket.PingDto
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class AuthRepository(private val context: Context) {
    private val message = "test".toByteArray()
    private val port = 6666
    private val tcpPort = 8888
    private val byteArray = ByteArray(5000)
    private val sharedPrefName = "sharedPref"
    private val userIdKey = "user_id"
    private val gson by lazy { Gson() }
    private val socket by lazy { Socket(getIpAddress(), tcpPort) }
    private val reader by lazy { BufferedReader(InputStreamReader(socket.getInputStream())) }
    private val writer by lazy { PrintWriter(OutputStreamWriter(socket.getOutputStream())) }


    private fun getIpAddress(): String {
        var isIpGotten = false
        var ip = ""
        while (isIpGotten) {
            val datagramSocket = DatagramSocket()
            val datagramPacket = DatagramPacket(
                message,
                message.size,
                InetAddress.getByName("255.255.255.255"),
                port
            )
            val datagramReceivePacket = DatagramPacket(byteArray, byteArray.size)
            datagramSocket.send(datagramPacket)
            ip = datagramSocket.receive(datagramReceivePacket).toString()
            if (ip.isNotEmpty()) {
                isIpGotten = true
            }
        }
        return ip
    }

    fun getAuthToken(name: String) {
        val connectedJsonData = reader.readLine()
        val userId = gson.fromJson(connectedJsonData, String::class.java).replace("//", "")
        val connectDto = gson.toJson(ConnectDto(userId, name))
        val baseConnectDto = BaseDto(BaseDto.Action.CONNECT, connectDto)
        val connectMessageToSocket = gson.toJson(baseConnectDto)
        saveUserId(userId)
        writer.println(connectMessageToSocket)
    }

    private fun saveUserId(userId: String) {
        context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit {
            putString(userIdKey, userId)
        }
    }

    fun sendPingCommand() {
        val pingDto = PingDto(
            context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
                .getString(userIdKey, "0")
                .toString()
        )
        val pingDtoPayload = gson.toJson(pingDto)
        val baseDtoForPing = BaseDto(BaseDto.Action.PING, pingDtoPayload)
        writer.println(baseDtoForPing)
        val response = gson.fromJson(reader.readLine(), BaseDto::class.java).action
        if (response != BaseDto.Action.PONG) {
            Socket(getIpAddress(), tcpPort)
        }
    }

}