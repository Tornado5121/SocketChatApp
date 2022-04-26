package com.natife.example.mysocketchatapp.ui.authScreen

import android.content.Context
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

    private val userNameKey = "myName"

    val name by lazy {
        requireContext()
            .getSharedPreferences("resources", Context.MODE_PRIVATE)
            .getString(userNameKey, "SomeName").toString()
    }

    private var isIpGExists = false

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
        if (!isIpGExists) {
            authViewModel.getIp()
            isIpGExists = true
        }
        binding.button.setOnClickListener {
            binding.progressBar.isVisible = true
            authViewModel.isUserAuth.observe(viewLifecycleOwner) {
                if (it) {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.fragment_container, UserListFragment())
                        .commit()
                }
            }
            authViewModel.launchReadSocketCommand(binding.editTextTextPersonName.text.toString())
            authViewModel.isAuthorised()
        }

        authViewModel.isIpExists.observe(viewLifecycleOwner) {
            if (it) {
                authViewModel.isAuthLoggingOn.observe(viewLifecycleOwner) { autoLog ->
                    if (autoLog) {
                        if (name.isNotEmpty() && name != "SomeName") {
                            requireActivity().supportFragmentManager
                                .beginTransaction()
                                .addToBackStack("")
                                .replace(R.id.fragment_container, UserListFragment())
                                .commit()

                            authViewModel.launchReadSocketCommand(name)
                        }
                    }
                    authViewModel.turnOffAutoLog()
                }
            }
        }
        authViewModel.isIpExist()
        authViewModel.turnOnAutoLog()
    }

}