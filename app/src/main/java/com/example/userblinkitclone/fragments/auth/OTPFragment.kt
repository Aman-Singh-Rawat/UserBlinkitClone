package com.example.userblinkitclone.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.userblinkitclone.R
import com.example.userblinkitclone.activities.UsersMainActivity
import com.example.userblinkitclone.databinding.FragmentOTPBinding
import com.example.userblinkitclone.models.Users
import com.example.userblinkitclone.util.Utils
import com.example.userblinkitclone.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {
    private var _binding: FragmentOTPBinding? = null
    private val binding: FragmentOTPBinding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private val userNumber by lazy { arguments?.getString("number") ?: "" }

    private var editTextArray: Array<EditText> = emptyArray()

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
        binding.otpToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }

        setUpEnteringOtp()
        sendOTP()
        onLoginButtonClicked()
    }

    private fun onLoginButtonClicked() {
        binding.btnLogin.setOnClickListener {
            Utils.showDialog(requireContext(), "Signing you...")
            val otp = editTextArray.joinToString("") { it.text.toString() }

            if (otp.length < editTextArray.size) {
                Utils.showToast(requireContext(), "Please enter right otp")
            } else {
                editTextArray.forEach { it.text?.clear(); it.clearFocus() }
                verifyOtp(otp)
            }
        }
    }

    private fun verifyOtp(otp: String) {
        Log.d("debugging", "userId is:: ${Utils.getUserCurrentId()}")
        val user = Users(
            uId = null,
            userPhoneNumber = userNumber,
            userAddress = null
        )
        viewModel.signInWithPhoneAuthCredential(
            otp, userNumber, user, //Parameters of signInWith.. method
            {
                Utils.hideDialog(); Utils.showToast(requireContext(), it)
                startActivity(Intent(requireActivity(), UsersMainActivity::class.java))
                requireActivity().finish()
            }, //Its onSuccess Method
            { Utils.hideDialog(); Utils.showToast(requireContext(), it) } // Its onFailure method
        )
    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP...")
        viewModel.sendOTP(userNumber, requireActivity())
        viewModel.apply {
            sendOTP(userNumber, requireActivity())
            lifecycleScope.launch {
                otpSent.collect {
                    if (it) {
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "Otp Successfully Send...")
                    }
                }
            }
        }
    }

    private fun setUpEnteringOtp() {
        editTextArray = arrayOf(
            binding.etOtpOne, binding.etOtpTwo,
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
                            if (index < editTextArray.size - 1)
                                editTextArray[index + 1].requestFocus()

                        } else if (s.length == 0) {
                            if (index > 0)
                                editTextArray[index - 1].requestFocus()
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