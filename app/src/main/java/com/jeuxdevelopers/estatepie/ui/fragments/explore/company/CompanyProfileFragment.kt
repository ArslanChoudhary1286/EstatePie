package com.jeuxdevelopers.estatepie.ui.fragments.explore.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentCompanyProfileBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsPropertyResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.explore.company.adapters.AdsPropertiesAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CompanyProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompanyProfileBinding
    private val viewModel: CompanyProfileViewModel by viewModels()
    var adapter = AdsPropertiesAdapter(::onAdsPropertiesItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompanyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCollectors()
        initClickListeners()
        initPropertyAdsRecycler()
        getAdsProperties()
    }

    private fun initCollectors() {
        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.adsPropertiesUiState){
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
                    waitingDialog.show(status = "Explore ads...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), "" + it.message)

//                    adapter.submitList(it.result?.data?.ad_propreties)

                }
            }
        }
    }

    private fun initClickListeners() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun getAdsProperties() {

        viewModel.getAdsProperties()
    }

    private fun initPropertyAdsRecycler() {
//        val adapter = AdsVerticalAdapter(::onItemClicked) {
//            findNavController().navigate(R.id.action_companyProfileFragment_to_adDetailFragment)
//        }
//        adapter.submitList(mutableListOf("1", "2", "3", "4", "5", "6", "7", "8"))
        binding.recyclerPropertyAds.adapter = adapter
    }

    private fun onAdsPropertiesItemClicked(item : AdsPropertyResponse.Data){

        val bundle = Bundle()
        bundle.putString(AppConsts.DETAIL_ARGUMENTS, Gson().toJson(item))
        findNavController().navigate(R.id.action_companyProfileFragment_to_adDetailFragment, bundle)

    }
}