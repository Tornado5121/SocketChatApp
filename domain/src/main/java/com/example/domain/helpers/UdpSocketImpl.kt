package com.example.domain.helpers

import kotlinx.coroutines.flow.MutableStateFlow
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

const val hostAddress ="255.255.255.255"
class UdpSocketImpl : UdpSocket{

    private val udpPort = 8888

    private val mIsIpGotten = MutableStateFlow(false)

    private val byteArray = ByteArray(5000)
    private val message = "getUdpConnection".toByteArray()

    override suspend fun getIpAddress(): String {
        var ip = ""
        while (ip.isEmpty()) {
            val datagramSocket = DatagramSocket()
            val datagramPacket = DatagramPacket(
                message,
                message.size,
                InetAddress.getByName(hostAddress),
                udpPort
            )
            val datagramReceivePacket = DatagramPacket(byteArray, byteArray.size)
            datagramSocket.send(datagramPacket)
            datagramSocket.receive(datagramReceivePacket)
            ip = datagramReceivePacket.address.hostAddress
        }
        mIsIpGotten.value = true
        return ip
    }

}