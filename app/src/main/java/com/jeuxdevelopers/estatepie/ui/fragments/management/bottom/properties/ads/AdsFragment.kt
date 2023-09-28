package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.ads

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentAdsBinding
import com.jeuxdevelopers.estatepie.models.shared.SharedViewModel
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AdsPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.CommercialPropertiesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.ads.adapters.AdsAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AdsFragment : Fragment() {

    private val viewModel: AdsViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var binding: FragmentAdsBinding

    private var adapter = AdsAdapter(::onItemClicked, ::onMenuItemClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdsBinding.inflate(inflater, container, false)

        initAdsRecycler()

        observeSharedModel()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAdsProperties()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCollectors()

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.adUiState){
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
                    waitingDialog.show(status = "Loading ...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    adapter.submitList(it.result?.data?.ad_propreties)

                    if (it.result?.data?.ad_propreties?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{

                        binding.emptyData.visibility = View.VISIBLE

                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.deleteAdsPropertyUiState){

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
                    Timber.d("Loading...")

                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()

                    ToastUtility.successToast(requireContext(), it.result?.message)
                    viewModel.getAdsProperties()

                }
            }
        }

    }

    private fun initAdsRecycler() {

        binding.recyclerAds.adapter = adapter

    }

    private fun observeSharedModel() {

        sharedViewModel.adsSearch.observe(viewLifecycleOwner, { search ->

            viewModel.adsRequest.search = search

            viewModel.getAdsProperties()

        })

        sharedViewModel.sortAds.observe(viewLifecycleOwner, { search ->

            viewModel.adsRequest.sort = search

            viewModel.getAdsProperties()

        })

    }

    private fun initClickListeners() {
    }

    private fun onMenuItemClick(proprety: AdsPropertiesResponse.Data.AdProprety, position: Int) {

        val pop= PopupMenu(requireContext(), binding.recyclerAds.getChildAt(position))
        pop.inflate(R.menu.property_menu)

        pop.setOnMenuItemClickListener {item->

            when(item.itemId)

            {
                R.id.edit->{
                    editProperty(proprety)                }

                R.id.delete->{

                    viewModel.deleteAdsRequest.id = proprety.id.toString()
                    viewModel.deleteAdsProperty()
                }

            }
            true
        }
        pop.show()

    }

    private fun editProperty(property: AdsPropertiesResponse.Data.AdProprety) {

//        val json = Gson().toJson(property).toString()
        val bundle = Bundle()
        bundle.putInt(AppConsts.PROPERTY_ID, property.id)

        findNavController().navigate(R.id.action_propertiesFragment_to_updateAdsFragment, bundle)

    }

    private fun onItemClicked(adProprety: AdsPropertiesResponse.Data.AdProprety) {

                val bundle = Bundle()
        bundle.putString(AppConsts.PROPERTY_ID, adProprety.id.toString())
        findNavController().navigate(R.id.action_propertiesFragment_to_managementAdDetailFragment, bundle)

    }

}