package com.jeuxdevelopers.estatepie.ui.dialogs.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.DialogSubscriptionBinding

class SubscriptionDialog: DialogFragment() {

    private lateinit var binding: DialogSubscriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSubscriptionBinding.inflate(inflater, container, false)

        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.image_bg);

        initClickListeners()

        return binding.root
    }

    private fun initClickListeners() {

        binding.dialogCross.setOnClickListener { dialog?.dismiss() }

        binding.dialogYesBtn.setOnClickListener {
            dialog?.dismiss()
            findNavController().navigate(R.id.action_requestsFragment_to_plansFragment2)
        }

    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}