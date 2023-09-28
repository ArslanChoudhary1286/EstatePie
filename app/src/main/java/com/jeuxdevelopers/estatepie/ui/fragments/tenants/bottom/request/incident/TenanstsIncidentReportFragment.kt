package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.request.incident

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenanstsIncidentReportBinding
import com.jeuxdevelopers.estatepie.models.shared.SharedViewModel
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.IncidentReportResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.incidentreport.adapter.TenantIncidentReportAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TenanstsIncidentReportFragment : Fragment() {

    private lateinit var binding: FragmentTenanstsIncidentReportBinding

    private val viewModel: TenanstsIncidentReportViewModel by viewModels()

    var adapter = TenantIncidentReportAdapter(::onItemClicked)

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenanstsIncidentReportBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getIncidentReports()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        observeSharedModel()

    }

    override fun onResume() {
        super.onResume()

        initCollectors()

        setAdapters()
    }

    private fun initClickListeners() {

    }

    private fun observeSharedModel() {

        sharedViewModel.refresh.observe(viewLifecycleOwner, { isRefresh ->

            if (isRefresh){
                sharedViewModel.refreshView(false)
                viewModel.getIncidentReports()
            }

        })
    }

    private fun setAdapters() {

        binding.recyclerIncidentReport.adapter = adapter

        if (viewModel.reportList.isNotEmpty()){

            binding.noDataFound.visibility = View.GONE

        }

        // all item adapter
        val allAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,R.id.adapterTxtView, resources.getStringArray(R.array.all)
        )
        binding.etAll.setAdapter(allAdapter)

        binding.etAll.setOnItemClickListener{parent, view, position, id ->

            var filterdList = ArrayList<IncidentReportResponse.Data.IncidentRequest>()

            if (position == 0){

                adapter.submitList(viewModel.reportList)

                if (viewModel.reportList.isEmpty() == false){

                    binding.noDataFound.visibility = View.GONE

                }else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

            } else if (position == 1){

                viewModel.reportList.forEachIndexed{index, data ->

                    if (data.status.equals("Under Review")){
                        filterdList.add(data)
                    }

                }
                adapter.submitList(filterdList)

                if (filterdList.isEmpty() == false){

                    binding.noDataFound.visibility = View.GONE

                }else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

            }else{

                viewModel.reportList.forEachIndexed{index, data ->

                    if (data.status.equals("Resolved")){
                        filterdList.add(data)
                    }

                }

                adapter.submitList(filterdList)

                if (filterdList.isEmpty() == false){

                    binding.noDataFound.visibility = View.GONE

                }else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

            }

        }

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.incidentReportsIdUiState){
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

                    it.result?.data?.incident_requests?.forEachIndexed{index, data ->
                        if (index == 0)
                            viewModel.reportList.clear()

                        viewModel.reportList.add(data)
                    }

                    adapter.submitList( it.result?.data?.incident_requests)

                    if (it.result?.data?.incident_requests?.isEmpty() == false){

                        binding.noDataFound.visibility = View.GONE

                    }else{
                        binding.noDataFound.visibility = View.VISIBLE
                    }

                }
            }
        }
    }

    private fun onItemClicked(item : IncidentReportResponse.Data.IncidentRequest){

        val bundle = Bundle()
        bundle.putString(AppConsts.INTENT_ID, item.id.toString())
        findNavController().navigate(R.id.action_tenantsRequestFragment_to_incidentTenantsDetailFragment, bundle)

    }

}