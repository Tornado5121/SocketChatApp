package com.natife.example.mysocketchatapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.natife.example.mysocketchatapp.R
import com.natife.example.mysocketchatapp.ui.authScreen.AuthFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel.isConnectionExists.observe(this) {
            if (!it) {
                transitToAuth()
            }
        }
    }

    private fun transitToAuth() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, AuthFragment())
            .commit()
    }

}