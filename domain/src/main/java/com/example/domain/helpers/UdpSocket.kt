package com.example.domain.helpers

interface UdpSocket {

    suspend fun getIpAddress(): String

}