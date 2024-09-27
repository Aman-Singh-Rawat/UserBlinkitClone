package com.example.userblinkitclone.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.userblinkitclone.R
import com.example.userblinkitclone.databinding.FragmentOTPBinding

class OTPFragment : Fragment() {
    private var _binding: FragmentOTPBinding? = null
    private val binding: FragmentOTPBinding get() = _binding!!

    private val userNumber by lazy { arguments?.getString("number") ?: ""}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.tvUserNumber.text = "+91 $userNumber"
        setUpEnteringOtp()
        binding.otpToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpEnteringOtp() {
        val editTextArray = arrayOf(binding.etOtpOne, binding.etOtpTwo,
            binding.etOtpThree, binding.etOtpFour,
            binding.etOtpFive, binding.etOtpSix
        )
        editTextArray.forEachIndexed { index, textInputEditText ->
            textInputEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        if (s.length == 1) {
                            if (index < editTextArray.size-1)
                                editTextArray[index+1].requestFocus()

                        } else if (s.length == 0) {
                            if (index > 0)
                                editTextArray[index-1].requestFocus()
                        }
                    }
                }

            })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}