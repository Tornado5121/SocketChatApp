package com.natife.example.mysocketchatapp.data

import android.content.Context
import com.google.gson.Gson
import com.natife.example.mysocketchatapp.data.socket.BaseDto
import com.natife.example.mysocketchatapp.data.socket.GetUsersDto
import com.natife.example.mysocketchatapp.data.socket.User
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class UserRepository(private val context: Context) {

    private val tcpPort = 8888
    private val sharedPrefName = "sharedPref"
    private val gson = Gson()

    fun getUserList(ip: String): List<User> {
        val socket = Socket(ip, tcpPort)
        val reader by lazy { BufferedReader(InputStreamReader(socket.getInputStream())) }
        val writer by lazy { PrintWriter(OutputStreamWriter(socket.getOutputStream())) }
        val getUsersDto = gson.toJson(GetUsersDto(context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
            .getString("user_id", "")
            .toString()))
        val baseUserListDto = BaseDto(BaseDto.Action.GET_USERS,getUsersDto )
        writer.println(baseUserListDto)
        return listOf(gson.fromJson(reader.readLine(), User::class.java))
    }

}
