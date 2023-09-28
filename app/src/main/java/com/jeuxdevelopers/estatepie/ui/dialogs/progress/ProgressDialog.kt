package com.jeuxdevelopers.estatepie.ui.dialogs.progress

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeuxdevelopers.estatepie.databinding.DialogProgressBinding

class ProgressDialog(context: Context) {

    private var alertDialog: AlertDialog? = null

    init {

        val binding: DialogProgressBinding = DialogProgressBinding
            .inflate(LayoutInflater.from(context))

        alertDialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root).setCancelable(false).create()
    }

    fun showDialogue() {
        alertDialog?.show()
    }

    fun hideDialogue() {
        alertDialog?.dismiss()
    }

}