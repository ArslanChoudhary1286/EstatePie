package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.paid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentPaidBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PaidBillsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.adapters.PaidBillsAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PaidBillsFragment : Fragment() {

    private lateinit var binding: FragmentPaidBillsBinding

    private val viewModel: PaidBillsViewModel  by viewModels()

    var adapter = PaidBillsAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaidBillsBinding.inflate(inflater, container, false)

        initCollectors()

        initRecycler()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getPaidBills()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.paidBillsUiState){
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

                    adapter.submitList(it.result?.data?.bills)

                    if (it.result?.data?.bills?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{

                        binding.emptyData.visibility = View.VISIBLE

                    }

                }
            }
        }
    }

    private fun initRecycler() {

        binding.recyclerPaidBills.adapter = adapter

    }

    private fun onItemClicked(item : PaidBillsResponse.Data.Bill){

        val bundle = Bundle()
        bundle.putString(AppConsts.PROFILE_ID, item.id.toString())
//
//        val navController = activity?.findNavController(R.id.nav_host_fragment_content_main_management)
        findNavController().navigate(R.id.action_billingFragment_to_paidBillsByTenantIdFragment, bundle)

    }

}