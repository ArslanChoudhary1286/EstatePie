package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential

import android.content.Context
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
import com.jeuxdevelopers.estatepie.databinding.FragmentResidentialBinding
import com.jeuxdevelopers.estatepie.models.shared.SharedViewModel
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.adapters.ResidentialAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ResidentialFragment : Fragment() {

    private lateinit var binding: FragmentResidentialBinding

    private val viewModel: ResidentialViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    var adapter = ResidentialAdapter(::onItemClicked, ::onMenuItemClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResidentialBinding.inflate(inflater, container, false)

        initCollectors()

        initResidentialRecycler()

        observeSharedModel()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getResidentialProperties()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.residentialUiState){
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

                    adapter.submitList(it.result?.data?.properties)

                    if (it.result?.data?.properties?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{

                        binding.emptyData.visibility = View.VISIBLE

                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.deleteResidentialPropertyUiState){
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
                    viewModel.getResidentialProperties()

                }
            }
        }

    }

    private fun initResidentialRecycler() {

        binding.recyclerResidential.adapter = adapter

    }

    private fun observeSharedModel() {

        sharedViewModel.residentialSearch.observe(viewLifecycleOwner, { search ->

            viewModel.residentialRequest.search = search

            viewModel.getResidentialProperties()

        })

        sharedViewModel.sortResidential.observe(viewLifecycleOwner, { search ->

            viewModel.residentialRequest.sort = search

            viewModel.getResidentialProperties()

        })

    }

    private fun initClickListeners() {

    }

    private fun onItemClicked(adProprety: ResidentialPropertiesResponse.Data.Property) {

        val bundle = Bundle()
        bundle.putString(AppConsts.PROPERTY_ID, adProprety.id.toString())
        findNavController().navigate(R.id.action_propertiesFragment_to_propertyDetailsFragment, bundle)

    }

    private fun onMenuItemClick(property: ResidentialPropertiesResponse.Data.Property, position: Int) {

        performOptionsMenuClick(property, position)
    }

    private fun performOptionsMenuClick(property: ResidentialPropertiesResponse.Data.Property, position: Int) {

        val pop= PopupMenu(requireContext(), binding.recyclerResidential.getChildAt(position))
        pop.inflate(R.menu.property_menu)

        pop.setOnMenuItemClickListener {item->

            when(item.itemId)

            {
                R.id.edit->{
                    editProperty(property)
                }

                R.id.delete->{

                    viewModel.deleteResidentialRequest.id = property.id.toString()
                    viewModel.deleteCommercialProperty()
                }

            }
            true
        }
        pop.show()

    }

    private fun editProperty(property: ResidentialPropertiesResponse.Data.Property) {

//        val json = Gson().toJson(property).toString()
        val bundle = Bundle()
        bundle.putInt(AppConsts.PROPERTY_ID, property.id)

        findNavController().navigate(R.id.action_propertiesFragment_to_updateResidentialFragment, bundle)

    }

}


