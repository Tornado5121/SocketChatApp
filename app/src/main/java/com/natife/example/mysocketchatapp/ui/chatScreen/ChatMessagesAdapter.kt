package com.natife.example.mysocketchatapp.ui.chatScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.natife.example.mysocketchatapp.data.ChatMessage
import com.natife.example.mysocketchatapp.databinding.ChatMessageItemBinding

class ChatMessagesAdapter :
    ListAdapter<
            ChatMessage,
            ChatMessagesAdapter.ChatMessageViewHolder>
        (ChatMessageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        return ChatMessageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ChatMessageViewHolder(private val binding: ChatMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ChatMessage) {
            with(binding) {
                userName.text = data.name
                userTextMessage.text = data.text_message
            }
        }

        companion object {
            fun from(parent: ViewGroup): ChatMessageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChatMessageItemBinding
                    .inflate(layoutInflater, parent, false)
                return ChatMessageViewHolder.from(binding.root)
            }
        }

    }

    class ChatMessageDiffUtil : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.id == newItem.id
        }

    }

}