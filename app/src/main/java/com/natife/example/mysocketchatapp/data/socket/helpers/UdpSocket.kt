package com.natife.example.mysocketchatapp.data.socket.helpers

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UdpSocket {

    private val udpPort = 8888

    private val byteArray = ByteArray(5000)
    private val message = "getUdpConnection".toByteArray()

    fun getIpAddress(): String? {
        var isIpGotten = false
        var ip: String? = ""
        while (!isIpGotten) {
            val datagramSocket = DatagramSocket()
            val datagramPacket = DatagramPacket(
                message,
                message.size,
                InetAddress.getByName("255.255.255.255"),
                udpPort
            )
            val datagramReceivePacket = DatagramPacket(byteArray, byteArray.size)
            datagramSocket.send(datagramPacket)
            datagramSocket.receive(datagramReceivePacket)
            ip = datagramReceivePacket.address.hostAddress
            if (ip.isNotEmpty()) {
                isIpGotten = true
            }
        }
        return ip
    }
}