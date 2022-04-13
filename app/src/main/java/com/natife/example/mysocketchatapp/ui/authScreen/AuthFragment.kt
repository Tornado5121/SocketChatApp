package com.natife.example.mysocketchatapp.ui.authScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.natife.example.mysocketchatapp.R
import com.natife.example.mysocketchatapp.databinding.FragmentAuthBinding
import com.natife.example.mysocketchatapp.ui.userListScreen.UserListFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.isVisible = false
        binding.button.setOnClickListener {
            binding.progressBar.isVisible = true
            authViewModel.launchReadSocketCommand(binding.editTextTextPersonName.text.toString())
            authViewModel.liveData.observe(viewLifecycleOwner) {
                if (it) {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, UserListFragment())
                        .commit()
                }
            }
            authViewModel.isAuthorised()
        }
    }

}