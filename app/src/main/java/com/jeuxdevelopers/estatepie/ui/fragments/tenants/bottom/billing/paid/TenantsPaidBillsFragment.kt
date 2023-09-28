package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.billing.paid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsPaidBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.billing.paid.adapter.TenantPaidBillsAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TenantsPaidBillsFragment : Fragment() {

    private lateinit var binding: FragmentTenantsPaidBillsBinding

    private val viewModel: TenantsPaidBillsViewModel by viewModels()

    var adapter = TenantPaidBillsAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsPaidBillsBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getUpcomingBills()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        setAdapters()
    }

    private fun setAdapters() {

        binding.recyclerUpcomingBills.adapter = adapter

    }

    private fun initClickListeners() {

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.upComingBillsUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
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

                    viewModel.listOfBillsResponse.clear()

                    it.result?.data?.maintenance_requests?.forEachIndexed{ index, data ->

                        if (data.status.equals("Paid"))
                            viewModel.listOfBillsResponse.add(data)

                    }

                    adapter.submitList(viewModel.listOfBillsResponse)

                    if (viewModel.listOfBillsResponse.size > 0){

                        binding.emptyData.visibility = View.GONE

                    }else{
                        binding.emptyData.visibility = View.VISIBLE
                    }

                }
            }
        }
    }

    private fun onItemClicked(item : ListOfBillsResponse.Data.MaintenanceRequest){

        val bundle = Bundle()
        bundle.putInt(AppConsts.INTENT_ID, item.id)
        findNavController().navigate(R.id.action_tenantsBillingFragment_to_unPaidBillDetailFragment, bundle)

    }

}