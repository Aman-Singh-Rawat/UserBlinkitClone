package com.example.userblinkitclone.fragments.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.userblinkitclone.R
import com.example.userblinkitclone.databinding.FragmentSignInBinding
import com.example.userblinkitclone.util.Utils

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding: FragmentSignInBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userNumberValidation()
        onContinueButtonClicked()
    }

    private fun onContinueButtonClicked() {
        binding.btnContinue.setOnClickListener {
            val number = binding.etUserNumber.text.toString()

            if (number.isEmpty() || number.length != 10)
                Utils.showToast(requireContext(), "Please enter valid phone number")
            else
                findNavController().navigate(
                    R.id.action_signInFragment_to_OTPFragment,
                    bundleOf("number" to number)
                )
        }
    }

    private fun userNumberValidation() {
        binding.etUserNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnContinue.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (s?.length == 10) R.color.green
                        else R.color.grayish_blue
                    )
                )
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}