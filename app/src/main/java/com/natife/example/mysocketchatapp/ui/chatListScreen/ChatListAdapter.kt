package com.natife.example.mysocketchatapp.ui.chatListScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.natife.example.mysocketchatapp.data.ChatUser
import com.natife.example.mysocketchatapp.databinding.ChatUserItemBinding

class ChatListAdapter(
    private val onCLick: (ChatUser) -> Unit
) :
    ListAdapter<ChatUser, ChatListAdapter.ChatUserViewHolder>(ItemDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserViewHolder {
        return ChatUserViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChatUserViewHolder, position: Int) {
        holder.bind(currentList[position], onCLick)

    }

    class ChatUserViewHolder(private val binding: ChatUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ChatUser, click: (ChatUser) -> Unit) {
            with (binding) {
                userName.text = data.name
            }
        }

        companion object {
            fun from(parent: ViewGroup): ChatUserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChatUserItemBinding
                    .inflate(layoutInflater, parent, false)
                return ChatUserViewHolder(binding)
            }
        }

    }

    class ItemDiffUtilCallback : DiffUtil.ItemCallback<ChatUser>() {
        override fun areItemsTheSame(oldItem: ChatUser, newItem: ChatUser): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatUser, newItem: ChatUser): Boolean {
            return oldItem.id == newItem.id
        }

    }

}