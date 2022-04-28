package com.natife.example.mysocketchatapp.data.repositories.userProfileRepo

import android.content.Context
import androidx.core.content.edit
import com.natife.example.mysocketchatapp.data.repositories.authRepo.SHARED_PREF_NAME
import com.natife.example.mysocketchatapp.data.repositories.authRepo.USER_NAME_KEY

class UserProfileRepositoryImpl(
    private val context: Context
) : UserProfileRepository {

    override fun saveName(name: String) {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit {
            putString(USER_NAME_KEY, name)
        }
    }

    override fun getName(): String {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            .getString(USER_NAME_KEY, "SomeName").toString()
    }

    override fun clearName() {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
    }

}

