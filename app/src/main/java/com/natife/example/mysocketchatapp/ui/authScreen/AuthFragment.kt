package com.natife.example.mysocketchatapp.ui.authScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.natife.example.mysocketchatapp.R
import com.natife.example.mysocketchatapp.databinding.FragmentAuthBinding
import com.natife.example.mysocketchatapp.ui.chatListScreen.ChatListFragment

class AuthFragment : Fragment() {
    lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ChatListFragment()).commit()
        }
    }
}