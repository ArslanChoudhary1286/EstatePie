package com.jeuxdevelopers.estatepie.ui.dialogs.signup

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.DialogSignupBottomSheetFBinding


class SignUpBottomSheetF(
    private val onEmailSignUp: () -> Unit,
    private val onAppleSignUp: () -> Unit,
    private val onFacebookSignUp: () -> Unit,
    private val onGoogleSignUp: () -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSignupBottomSheetFBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.CustomDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSignupBottomSheetFBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        // dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.btnEmailSingUp.setOnClickListener {
            onEmailSignUp()
            dismiss()
        }
        binding.btnAppleSignUp.setOnClickListener {
            onAppleSignUp()
            dismiss()
        }

        binding.btnFacebookSignUp.setOnClickListener {
            onFacebookSignUp()
            dismiss()
        }

        binding.btnGoogleSignUp.setOnClickListener {
            onGoogleSignUp()
            dismiss()
        }
    }
}