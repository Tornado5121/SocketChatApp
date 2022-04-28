package com.natife.example.mysocketchatapp.ui.chatScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.example.mysocketchatapp.data.repositories.authRepo.AuthRepository
import com.natife.example.mysocketchatapp.data.repositories.chatRepo.ChatRepository
import com.natife.example.mysocketchatapp.data.repositories.userRepo.UserRepository
import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import com.natife.example.mysocketchatapp.data.socket.models.SendMessageDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import com.natife.example.mysocketchatapp.data.socket.models.toMessageDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val userSpeakToId: String
) : ViewModel() {

    private val mMessagesLiveData = MutableLiveData<List<MessageDto>>()
    val messageLiveData = mMessagesLiveData

    private var messages = listOf<MessageDto>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.messageFlow.collectLatest {
                if (it.from.id == userSpeakToId) {
                    messages = messages + it
                }
                mMessagesLiveData.postValue(messages)
            }
        }

        viewModelScope.launch {
            chatRepository.getNewMessage()
        }
    }

    fun getMyId(): String {
        return authRepository.id
    }

    fun sendMessage(myMessage: SendMessageDto) {
        val message = myMessage.toMessageDto()
        messages = messages + message
        mMessagesLiveData.postValue(messages)

        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.sendMessage(
                myMessage.receiver,
                myMessage.message,
                authRepository.id
            )
        }
    }

    fun getUser(id: String?): User {
        val userList = userRepository.userListFlow.value
        val user = userList.filter {
            it.id == id
        }[0]
        return user
    }

}