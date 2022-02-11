package com.natife.example.mysocketchatapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.natife.example.mysocketchatapp.R
import com.natife.example.mysocketchatapp.ui.authScreen.AuthFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, AuthFragment())
            .commit()
    }

}