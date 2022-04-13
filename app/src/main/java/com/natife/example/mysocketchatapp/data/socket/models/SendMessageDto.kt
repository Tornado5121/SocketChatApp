package com.natife.example.mysocketchatapp.data.socket.models

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload

fun SendMessageDto.toMessageDto() : MessageDto {
    return MessageDto(
        from  = User(id, "my message"),
        message = message
    )
}