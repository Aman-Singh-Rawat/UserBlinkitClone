package com.example.userblinkitclone.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.userblinkitclone.models.Users
import com.example.userblinkitclone.util.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel: ViewModel() {
    private val _verificationId = MutableStateFlow<String?>(null)
    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent

    fun sendOTP(userNumber: String, activity: Activity) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("debugging","Verification failed: ${e.message}")
                _otpSent.value = false
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                _verificationId.value = verificationId
                _otpSent.value = true
                Log.d("debugging", "Verification code sent successfully.")
            }
        }

        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber("+91$userNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(
        otp: String, userNumber: String, user: Users,
        onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {

        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)

        Log.d("debugging", "userId in signInWithPhoneAuthCredential :: ${Utils.getUserCurrentId()}")

        Utils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Successful login
                    Log.d("debugging", "task.isSuccessful :: ${Utils.getUserCurrentId()}")
                    Log.d("debugging", "uId is:: ${user.uId}")
                    Log.d("debugging", "address is:: ${user.userAddress}")
                    Log.d("debugging", "phone number is:: ${user.userPhoneNumber}")

                    if (user.uId != null) {
                        Log.d("debugging", "user id is not null")
                        FirebaseDatabase.getInstance("https://blinkit-clone-b8338-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("AllUsers")
                            .child("Users")
                            .child(user.uId)
                            .setValue(user)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Log.d("debugging", "Data written successfully")
                                    onSuccess.invoke("Successfully Logged In")
                                } else {
                                    Log.e("debugging", "Failed to write to database: ${dbTask.exception?.message}")
                                    onFailure.invoke(dbTask.exception?.message.toString())
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("debugging", "Database operation failed: ${exception.message}")
                                onFailure.invoke(exception.message.toString())
                            }
                    } else {
                        Log.d("debugging", "user id is null")
                    }
                } else {
                    Log.e("debugging", task.exception?.message.toString())
                    onFailure.invoke(task.exception?.message.toString())
                }
            }
    }
}