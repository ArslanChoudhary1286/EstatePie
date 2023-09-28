package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports.requestsByProperty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentRequestsByPropertyIdBinding
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.RequestsByPropertyIdResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports.adapter.RequestByPropertyIdAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RequestsByPropertyIdFragment : Fragment() {

    private lateinit var binding: FragmentRequestsByPropertyIdBinding

    private val viewModel: RequestsByPropertyIdViewModel by viewModels()

    var adapter = RequestByPropertyIdAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestsByPropertyIdBinding.inflate(inflater, container, false)

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

        setUpViewsFromBundle()

        setAdapter()

    }

    private fun setUpViewsFromBundle() {

        binding.tvTitle.text = arguments?.getString(AppConsts.TITLE, "Reports").toString()

    }

    private fun initIntentData() {

        viewModel.request.id = arguments?.getString(AppConsts.INTENT_ID, "").toString()

        viewModel.getRequestByPropertyId()

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }
    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.requestsByPropertyIdUiState){
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

                    adapter.submitList(it.result?.data?.requests)

                }
            }
        }

    }

    private fun setAdapter() {

        binding.recyclerRequestByPropertyId.adapter = adapter

    }

    private fun onItemClicked(item : RequestsByPropertyIdResponse.Data.Request){

        val bundle = Bundle()
//        bundle.putString(AppConsts.INTENT_ID, item.id.toString())
        bundle.putString(AppConsts.DETAIL_ARGUMENTS, Gson().toJson(item))
        findNavController().navigate(R.id.action_requestsByPropertyIdFragment_to_allReportsDetailFragment, bundle)

    }


}