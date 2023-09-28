package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.commercial

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
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentCommercialBinding
import com.jeuxdevelopers.estatepie.models.shared.SharedViewModel
import com.jeuxdevelopers.estatepie.network.responses.management.properties.CommercialPropertiesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.commercial.adapters.CommercialAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CommercialFragment : Fragment() {

    private lateinit var binding: FragmentCommercialBinding

    private val viewModel: CommercialViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    private var adapter = CommercialAdapter(::onItemClicked, ::onMenuItemClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommercialBinding.inflate(inflater, container, false)

        initCollectors()

        initCommercialRecycler()

        observeSharedModel()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getCommercialProperties()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.commercialUiState){
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

                    adapter.submitList(it.result?.data?.propreties)

                    if (it.result?.data?.propreties?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{
                        binding.emptyData.visibility = View.VISIBLE
                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.deleteCommercialPropertyUiState){
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
                    viewModel.getCommercialProperties()

                }
            }
        }

    }

    private fun initCommercialRecycler() {

        binding.recyclerCommercial.adapter = adapter

    }

    private fun observeSharedModel() {

        sharedViewModel.commercialSearch.observe(viewLifecycleOwner, { search ->

            viewModel.commercialRequest.search = search

            viewModel.getCommercialProperties()

        })

        sharedViewModel.sortCommercial.observe(viewLifecycleOwner, { search ->

            viewModel.commercialRequest.sort = search

            viewModel.getCommercialProperties()

        })

    }

    private fun initClickListeners() {
    }

    private fun onMenuItemClick(proprety: CommercialPropertiesResponse.Data.Proprety, position: Int) {


        val pop= PopupMenu(requireContext(), binding.recyclerCommercial.getChildAt(position))
        pop.inflate(R.menu.property_menu)

        pop.setOnMenuItemClickListener {item->

            when(item.itemId)

            {
                R.id.edit->{
                    editProperty(proprety)                }

                R.id.delete->{

                    viewModel.deleteCommercialRequest.id = proprety.id.toString()
                    viewModel.deleteCommercialProperty()
                }

            }
            true
        }
        pop.show()

    }

    private fun editProperty(property: CommercialPropertiesResponse.Data.Proprety) {

//        val json = Gson().toJson(property).toString()
        val bundle = Bundle()
        bundle.putInt(AppConsts.PROPERTY_ID, property.id)

        findNavController().navigate(R.id.action_propertiesFragment_to_updateCommercialFragment, bundle)

    }

    private fun onItemClicked(adProprety: CommercialPropertiesResponse.Data.Proprety) {

        val bundle = Bundle()
        bundle.putString(AppConsts.PROPERTY_ID, adProprety.id.toString())
        findNavController().navigate(R.id.action_propertiesFragment_to_propertyDetailsFragment, bundle)

    }

}