package com.natife.example.mysocketchatapp.data.socket

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload