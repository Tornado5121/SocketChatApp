package com.natife.example.mysocketchatapp.ui.chatScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.natife.example.mysocketchatapp.data.socket.helpers.TcpSocket
import com.natife.example.mysocketchatapp.data.socket.models.MessageDto
import com.natife.example.mysocketchatapp.databinding.MyChatMessageItemBinding
import com.natife.example.mysocketchatapp.databinding.NotmyChatMessageItemBinding

private const val VIEW_TYPE_ONE = 1
private const val VIEW_TYPE_TWO = 2

class ChatMessagesAdapter(
    private val tcpSocket: TcpSocket
) : ListAdapter<
        MessageDto,
        RecyclerView.ViewHolder>
    (ChatMessageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ONE) {
            MyChatMessageViewHolder.from(parent)
        } else {
            NotMyChatMessageViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (currentList[position].from.id == tcpSocket.id) {
            (holder as MyChatMessageViewHolder).bind(currentList[position])
        } else (holder as NotMyChatMessageViewHolder).bind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].from.id == tcpSocket.id) {
            VIEW_TYPE_ONE
        } else {
            VIEW_TYPE_TWO
        }
    }

    class MyChatMessageViewHolder(
        private val binding: MyChatMessageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MessageDto) {
            with(binding) {
                userName.text = data.from.name
                userTextMessage.text = data.message
            }
        }

        companion object {

            fun from(parent: ViewGroup): MyChatMessageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MyChatMessageItemBinding
                    .inflate(layoutInflater, parent, false)
                return MyChatMessageViewHolder(binding)
            }

        }

    }

    class NotMyChatMessageViewHolder(
        private val binding: NotmyChatMessageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MessageDto) {
            with(binding) {
                userName.text = data.from.name
                userTextMessage.text = data.message
            }
        }

        companion object {

            fun from(parent: ViewGroup): NotMyChatMessageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotmyChatMessageItemBinding
                    .inflate(layoutInflater, parent, false)
                return NotMyChatMessageViewHolder(binding)
            }

        }

    }

    class ChatMessageDiffUtil : DiffUtil.ItemCallback<MessageDto>() {

        override fun areItemsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem.message == newItem.message
        }

    }

}