package com.natife.example.mysocketchatapp.ui.chatScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.helpers.UdpSocket
import com.natife.example.mysocketchatapp.data.socket.models.SendMessageDto
import com.natife.example.mysocketchatapp.databinding.FragmentChatBinding
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val chatViewModel by viewModel<ChatViewModel>()
    private val tcpSocket by lazy { TcpSocket(requireContext(), UdpSocket()) }
    private val chatMessageAdapter by lazy { ChatMessagesAdapter(tcpSocket) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
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
        chatViewModel.onGetNewMessage()

        chatViewModel.liveData.observe(viewLifecycleOwner) { userList ->
            chatMessageAdapter.submitList(userList)

            chatViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                binding.button.setOnClickListener {
                    binding.recyclerView.scrollToPosition(userList.size - 1)
                    val message = binding.editText.text.toString()
                    if (message.isNotEmpty()) {
                        chatViewModel.onSendMessage(
                            SendMessageDto(
                                tcpSocket.id,
                                user.id,
                                message
                            )
                        )
                    }
                    binding.editText.setText("")
                }
            }
        }
        arguments?.let { chatViewModel.getUser(it.getString(KEY_ID).toString()) }
    }


    companion object {

        private const val KEY_ID: String = "user_id"

        fun getChatFragmentInstance(id: String): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ID, id)
                }
            }
        }

    }

}
