package com.natife.example.mysocketchatapp.data.repositories.userProfileRepo

import android.content.SharedPreferences
import androidx.core.content.edit

const val USER_NAME_KEY = "myName"

class UserProfileRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : UserProfileRepository {

    override fun saveName(name: String) {
        sharedPrefs.edit {
            putString(USER_NAME_KEY, name)
        }
    }

    override fun getName(): String {
        return sharedPrefs
            .getString(USER_NAME_KEY, "SomeName").toString()
    }

    override fun clearName() {
        sharedPrefs.edit().clear().apply()
    }

}

