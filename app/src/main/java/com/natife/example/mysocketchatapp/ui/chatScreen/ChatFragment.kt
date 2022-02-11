package com.natife.example.mysocketchatapp.ui.chatScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.natife.example.mysocketchatapp.databinding.FragmentChatBinding

class ChatFragment: Fragment() {

    lateinit var binding: FragmentChatBinding

    private  val chatMessageAdapter = ChatMessagesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with (binding) {
            recyclerView.adapter = chatMessageAdapter
            button.setOnClickListener {
                //TODO make sending text messages
//                editText.text
            }
        }
    }
}