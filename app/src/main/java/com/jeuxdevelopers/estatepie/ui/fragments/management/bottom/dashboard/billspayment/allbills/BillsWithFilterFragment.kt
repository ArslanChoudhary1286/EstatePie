package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.billspayment.allbills

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentBillsWithFilterBinding
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.PropertyBillsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.billspayment.adapter.BillsWithFilterAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BillsWithFilterFragment : Fragment() {

    private lateinit var binding: FragmentBillsWithFilterBinding

    private val viewModel: BillsWithFilterViewModel by viewModels()

    var adapter = BillsWithFilterAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillsWithFilterBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initIntentData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        setAdapter()

    }

    private fun initIntentData() {

        viewModel.request.id = arguments?.getString(AppConsts.INTENT_ID, "").toString()

        viewModel.getPropertyBills()

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }
    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.propertyBillsUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), "Error : ${it.message}, ${it.result.toString()}")

                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    adapter.submitList(it.result?.data?.bills)

                }
            }
        }

    }

    private fun setAdapter() {

        binding.recyclerBillsWithFilter.adapter = adapter

    }

    private fun onItemClicked(item : PropertyBillsResponse.Data.Bill){

        val bundle = Bundle()
//        bundle.putString(AppConsts.PROFILE_ID, item.id.toString())
        bundle.putString(AppConsts.BILL_MODEL, Gson().toJson(item))
        findNavController().navigate(R.id.action_billsWithFilterFragment_to_propertyBillsDetailFragment, bundle)

    }

}