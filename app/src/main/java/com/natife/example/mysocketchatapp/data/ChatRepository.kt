package com.natife.example.mysocketchatapp.data

import android.content.Context
import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.BaseDto
import com.natife.example.mysocketchatapp.data.socket.SendMessageDto
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class ChatRepository(private val context: Context) {

    private val tcpPort = 8888
    private val sharedPrefName = "sharedPref"
    private val gson = Gson()

    fun sendMessage(ip: String, receiverId: String, message: String) {
        val socket = Socket(ip, tcpPort)
        val writer by lazy { PrintWriter(OutputStreamWriter(socket.getOutputStream())) }
        val sendMessageDto = gson.toJson(
            SendMessageDto(context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
            .getString("user_id", "")
            .toString(), receiverId, message )
        )
        val baseUserListDto = BaseDto(BaseDto.Action.SEND_MESSAGE,sendMessageDto )
        writer.println(baseUserListDto)
    }

    fun getNewMessage(ip: String): ChatMessage {
        val socket = Socket(ip, tcpPort)
        val reader by lazy { BufferedReader(InputStreamReader(socket.getInputStream())) }
        val newMessage = reader.readLine()
        return gson.fromJson(newMessage, ChatMessage::class.java)
    }

}