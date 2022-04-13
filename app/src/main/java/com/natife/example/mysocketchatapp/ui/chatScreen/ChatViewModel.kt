package com.natife.example.mysocketchatapp.ui.chatScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.example.mysocketchatapp.data.repositories.ChatRepository
import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import com.natife.example.mysocketchatapp.data.socket.models.SendMessageDto
import com.natife.example.mysocketchatapp.data.socket.models.User
import com.natife.example.mysocketchatapp.data.socket.models.toMessageDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val mMessagesLiveData = MutableLiveData<List<MessageDto>>()
    val liveData = mMessagesLiveData

    private val mUserLiveData = MutableLiveData<User>()
    val userLiveData = mUserLiveData

    private var messages = listOf<MessageDto>()

    private val myScope = CoroutineScope(Job() + Dispatchers.IO)
    private val mySecondScope = CoroutineScope(Job() + Dispatchers.IO)
    private val myThirdScope = CoroutineScope(Job() + Dispatchers.IO)

    fun onSendMessage(myMessage: SendMessageDto) {
        val message = myMessage.toMessageDto()
        messages = messages + message
        myScope.launch {
            mMessagesLiveData.postValue(messages)
        }

        mySecondScope.launch {
            chatRepository.sendMessage(myMessage.receiver, myMessage.message)
        }
    }

    fun onGetNewMessage() {
        myThirdScope.launch {
            chatRepository.tcpSocket.messageFlow.collectLatest {
                messages = messages + it
                mMessagesLiveData.postValue(messages)
            }
        }
    }

    fun getUser(id: String) {
        myScope.launch {
            chatRepository.tcpSocket.userFlow.collect {
                mUserLiveData.postValue(it)
            }
        }

        viewModelScope.launch {
            chatRepository.getUserById(id)
        }
    }

}