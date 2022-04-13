package com.natife.example.mysocketchatapp.ui.userListScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.example.mysocketchatapp.R
import com.natife.example.mysocketchatapp.databinding.FragmentUserListBinding
import com.natife.example.mysocketchatapp.ui.chatScreen.ChatFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private val userListViewModel by viewModel<UserListViewModel>()

    private val userListAdapter = UserListAdapter {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .addToBackStack("")
            .replace(R.id.fragment_container, ChatFragment.getChatFragmentInstance(it.id))
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            progressBar2.isVisible = true
            recyclerView.adapter = userListAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }

        userListViewModel.sendGetUserCommand()
        userListViewModel.liveData.observe(viewLifecycleOwner) {
            userListAdapter.submitList(it)
        }
        userListViewModel.getUserList()
        binding.progressBar2.isVisible = false
    }

}