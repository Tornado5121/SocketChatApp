package com.natife.example.mysocketchatapp.data.socket.models

data class UsersReceivedDto(val users: List<User>) : Payload

data class User(val id: String, val name: String)