package com.natife.example.mysocketchatapp.data.socket

data class MessageDto(val from: User, val message: String) : Payload