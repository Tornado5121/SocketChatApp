package com.natife.example.mysocketchatapp.ui.chatScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.example.mysocketchatapp.data.socket.models.SendMessageDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import com.natife.example.mysocketchatapp.databinding.FragmentChatBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

const val USER_ID_KEY = "user_id"

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val chatViewModel: ChatViewModel by viewModel {
        parametersOf(
            requireArguments().getString(
                USER_ID_KEY
            )
        )
    }
    private lateinit var userId: String
    private val user: User by lazy { User(userId, "") }
    private val chatMessageAdapter by lazy {
        ChatMessagesAdapter(user) {
            it.id
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        userId = arguments?.let { chatViewModel.getUser(it.getString(KEY_ID)) }?.id ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerView.adapter = chatMessageAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            layoutManager.stackFromEnd = true
            recyclerView.layoutManager = layoutManager
        }

        chatViewModel.messageLiveData.observe(viewLifecycleOwner) { userList ->
            chatMessageAdapter.submitList(userList)
        }

        binding.button.setOnClickListener {
            val message = binding.editText.text.toString()
            if (message.isNotEmpty()) {
                chatViewModel.sendMessage(
                    SendMessageDto(
                        chatViewModel.getMyId(),
                        user.id,
                        message
                    )
                )
            }
            binding.recyclerView.scrollToPosition(chatMessageAdapter.itemCount - 1)
            binding.editText.setText("")
        }
    }

    companion object {

        private const val KEY_ID: String = USER_ID_KEY

        fun getChatFragmentInstance(id: String): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ID, id)
                }
            }
        }

    }

}