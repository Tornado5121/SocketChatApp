package com.natife.example.mysocketchatapp.ui.userListScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.example.mysocketchatapp.R
import com.natife.example.mysocketchatapp.databinding.FragmentUserListBinding
import com.natife.example.mysocketchatapp.ui.authScreen.AuthFragment
import com.natife.example.mysocketchatapp.ui.chatScreen.ChatFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private val userListViewModel by viewModel<UserListViewModel>()

    private val userListAdapter by lazy {
        UserListAdapter {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack("")
                .replace(R.id.fragment_container, ChatFragment.getChatFragmentInstance(it.id))
                .commit()
        }
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
            recyclerView.adapter = userListAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }
        userListViewModel.userListLiveData.observe(viewLifecycleOwner) {
            userListAdapter.submitList(it)
        }

        binding.button2.setOnClickListener {
            userListViewModel.logOut()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack("")
                .replace(R.id.fragment_container, AuthFragment())
                .commit()
        }
    }

}