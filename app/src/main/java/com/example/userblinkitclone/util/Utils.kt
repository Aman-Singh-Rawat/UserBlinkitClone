package com.example.userblinkitclone.util

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.userblinkitclone.databinding.ProgressDialogBinding
import com.google.firebase.auth.FirebaseAuth

object Utils {
    private var dialog: AlertDialog? = null

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showDialog(context: Context, message: String) {
        val progress = ProgressDialogBinding
            .inflate(LayoutInflater.from(context))

        progress.tvMessage.text = message

        dialog = AlertDialog.Builder(context)
            .setView(progress.root)
            .setCancelable(false)
            .create()

        dialog!!.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }

    private var firebaseAuthInstance: FirebaseAuth? = null
    fun getAuthInstance(): FirebaseAuth {
        if (firebaseAuthInstance == null)
            firebaseAuthInstance = FirebaseAuth.getInstance()

        return firebaseAuthInstance!!
    }
}