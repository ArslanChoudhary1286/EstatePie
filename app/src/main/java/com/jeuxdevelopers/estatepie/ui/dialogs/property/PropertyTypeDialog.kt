package com.jeuxdevelopers.estatepie.ui.dialogs.property

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.DialogPropertyTypeBinding
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertyTypeResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.others.PropertyTypesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.property.adapter.PropertyTypesAdapter
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PropertyTypeDialog(
    private val onSelectedType: (type: PropertyTypeResponse.Data) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogPropertyTypeBinding

    private val viewModel: PropertyTypeViewModel by viewModels()

    private val waitingDialog: WaitingDialog by lazy { WaitingDialog(requireContext()) }

    private lateinit var adapter: PropertyTypesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPropertyTypeBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initClickListeners()
        initCollectors()
    }

    private fun initAdapters() {
        adapter = PropertyTypesAdapter {
            onSelectedType.invoke(it)
            dismiss()
        }
        binding.recyclerTypes.adapter = adapter
    }

    private fun initCollectors() {
        collectLatestLifecycleFlow(viewModel.propertyTypes) {
            when (it) {
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    ToastUtility.errorToast(requireContext(), it.message.toString())
                    Timber.d("Error: API error -> ${it.message.toString()}")
                }
                is NetworkResult.Idle -> {}
                is NetworkResult.Loading -> {
                    waitingDialog.show("Loading")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success: data -> ${it.result?.data?.size}")
                    adapter.submitList(it.result?.data)
                }
            }
        }
    }

    private fun initClickListeners() {
        binding.dialogCross.setOnClickListener { dismiss() }
    }

}