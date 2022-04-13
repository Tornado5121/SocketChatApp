package com.natife.example.mysocketchatapp.data.socket.models

data class MessageDto(val from: User, val message: String) : Payload