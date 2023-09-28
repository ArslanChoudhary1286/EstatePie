package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.upcomingbills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentUpcomingBillsBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.GetBillTypesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.upcomingbills.adapter.UpComingBillsAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UpcomingBillsFragment : Fragment() {

    private lateinit var binding: FragmentUpcomingBillsBinding

    private val viewModel: UpcomingBillsViewModel by viewModels()

    var adapter = UpComingBillsAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getBillTypes()

        viewModel.getUpcomingBills()

    }

    override fun onResume() {
        super.onResume()

        initCollectors()

        setAdapters()

    }

    private fun setAdapters() {

        binding.recyclerUpcomingBills.adapter = adapter

        // All status adapter
        val statusAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter_border,
            R.id.adapterTxtView, resources.getStringArray(R.array.all_status)
        )
        binding.etAllStatus.setAdapter(statusAdapter)


        binding.etAllStatus.setOnItemClickListener{parent, view, position, id ->

            val status = binding.etAllStatus.text.toString()

            if (status.equals("All Status")){

                viewModel.request.status = ""
            }
            else {

                viewModel.request.status = status
            }

            viewModel.getUpcomingBills()

        }

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

                    adapter.submitList(it.result?.data?.maintenance_requests)

                    if (it.result?.data?.maintenance_requests?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{
                        binding.emptyData.visibility = View.VISIBLE
                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.billTypesUiState){
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

                    setBillTypesAdapter(it.result?.data)

                }
            }
        }

    }

    private fun setBillTypesAdapter(data: List<GetBillTypesResponse.Data>?) {

        val billsTypeList = ArrayList<String>()

        data?.forEachIndexed{index, billName ->

            if (index == 0)
                billsTypeList.add(getString(R.string.all_bills))

            billsTypeList.add(billName.name)

        }

        // All bills adapter
        val billAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter_border,
            R.id.adapterTxtView, billsTypeList
        )

        binding.etAllBills.setAdapter(billAdapter)

        binding.etAllBills.setOnItemClickListener{parent, view, position, id ->

            if (position == 0)
                viewModel.request.bill_type_id = ""
            else
                viewModel.request.bill_type_id = data!![position-1].id.toString()

            viewModel.getUpcomingBills()

        }

    }

    private fun onItemClicked(item : ListOfBillsResponse.Data.MaintenanceRequest){

        val bundle = Bundle()
        bundle.putInt(AppConsts.INTENT_ID, item.id)
        findNavController().navigate(R.id.action_tenantsDashboardFragment_to_unPaidBillDetailFragment, bundle)

    }

}