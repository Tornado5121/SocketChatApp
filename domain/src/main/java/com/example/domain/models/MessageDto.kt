package com.example.domain.models

data class MessageDto(val from: User, val message: String) : Payload