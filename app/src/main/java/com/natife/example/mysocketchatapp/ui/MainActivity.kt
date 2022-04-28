package com.natife.example.mysocketchatapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.natife.example.mysocketchatapp.R
import com.natife.example.mysocketchatapp.ui.authScreen.AuthFragment
import com.natife.example.mysocketchatapp.ui.authScreen.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        authViewModel.getIp()
        authViewModel.isConnectionExists.observe(this) {
            if (!it) {
                transitToAuth()
            }
        }
        authViewModel.isConnectionExists()
    }

    private fun transitToAuth() {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("")
            .replace(R.id.fragment_container, AuthFragment())
            .commit()
    }

}