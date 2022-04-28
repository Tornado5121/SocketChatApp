package com.natife.example.mysocketchatapp.data.repositories.userProfileRepo

interface UserProfileRepository {

    fun saveName(name: String)
    fun getName(): String
    fun clearName()

}