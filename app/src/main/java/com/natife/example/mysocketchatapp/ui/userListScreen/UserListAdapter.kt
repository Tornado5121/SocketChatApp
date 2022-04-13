package com.natife.example.mysocketchatapp.ui.userListScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.natife.example.mysocketchatapp.data.socket.models.User
import com.natife.example.mysocketchatapp.databinding.UserItemBinding

class UserListAdapter(
    private val onCLick: (User) -> Unit
) :
    ListAdapter<User, UserListAdapter.UserViewHolder>(ItemDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(currentList[position], onCLick)
    }

    class UserViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: User, click: (User) -> Unit) {
            with(binding) {
                userName.text = data.name
                root.setOnClickListener {
                    click(data)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserItemBinding
                    .inflate(layoutInflater, parent, false)
                return UserViewHolder(binding)
            }
        }

    }

    class ItemDiffUtilCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

}