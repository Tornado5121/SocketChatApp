package com.natife.example.mysocketchatapp.data.socket.helpers

interface UdpSocket {

    suspend fun getIpAddress(): String

}