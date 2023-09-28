package com.jeuxdevelopers.estatepie.ui.dialogs.delete

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.DialogDeleteAccountBinding
import com.jeuxdevelopers.estatepie.databinding.DialogLogoutBinding

class DeleteAccountDialog(context: Context) {

    private var alertDialog: AlertDialog? = null

    init {

        val binding: DialogDeleteAccountBinding = DialogDeleteAccountBinding
            .inflate(LayoutInflater.from(context))

        alertDialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root).setCancelable(true).create()
    }

    fun showDialogue() {
        alertDialog?.show()
    }

    fun hideDialogue() {
        alertDialog?.dismiss()
    }

    fun getYesView() : TextView? {
       return alertDialog?.findViewById<TextView>(R.id.tv_yes)
    }

    fun getNoView() : TextView? {
        return alertDialog?.findViewById<TextView>(R.id.tv_no)
    }

}