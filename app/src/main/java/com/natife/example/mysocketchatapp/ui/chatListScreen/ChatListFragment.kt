package com.natife.example.mysocketchatapp.ui.chatListScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.natife.example.mysocketchatapp.R
import com.natife.example.mysocketchatapp.databinding.FragmentChatListBinding
import com.natife.example.mysocketchatapp.ui.chatScreen.ChatFragment

class ChatListFragment : Fragment() {

    lateinit var binding: FragmentChatListBinding

    private val chatListAdapter = ChatListAdapter {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChatFragment()).commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = chatListAdapter
//        chatListAdapter.submitList()
    }

}