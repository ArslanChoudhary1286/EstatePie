package com.jeuxdevelopers.estatepie.ui.fragments.auth.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnNext.setOnClickListener {

            validateInputs()
        }

        binding.btnLogin.setOnClickListener {

            if (findNavController().previousBackStackEntry?.destination?.id == R.id.loginFragment) {
                findNavController().navigateUp()
            } else {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }

        }

    }

    private fun validateInputs() {

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPass = binding.etConfirmPassword.text.toString()

        if (firstName.isEmpty()) {
            binding.etFirstName.error = "Please enter your first name."
        } else if (lastName.isEmpty()) {
            binding.etLastName.error = "Please enter your last name."
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etEmailAddress.error = "Email is empty or invalid."

        } else if (password.isEmpty() || password.length < 6) {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etEmailAddress.error = null
            binding.etPassword.error = "Password is too short or empty."
        } else if (confirmPass.isEmpty() || confirmPass != password) {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etEmailAddress.error = null
            binding.etPassword.error = null
            binding.etConfirmPassword.error = "Password does not match."
        } else {
            binding.etFirstName.error = null
            binding.etLastName.error = null
            binding.etEmailAddress.error = null
            binding.etPassword.error = null
            binding.etConfirmPassword.error = null

            val bundle = Bundle()
            bundle.putString("first_name",firstName)
            bundle.putString("last_name",lastName)
            bundle.putString("email",email)
            bundle.putString("password",password)
            bundle.putString("confirm_password",confirmPass)

            findNavController().navigate(R.id.action_signUpFragment_to_signUp2Fragment, bundle)

        }
    }

}