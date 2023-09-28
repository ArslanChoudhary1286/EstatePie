package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile.vehicle

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsVehicleBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.GetVehicleResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile.vehicle.adapter.VehiclesAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TenantsVehicleFragment : Fragment() {

    private lateinit var binding: FragmentTenantsVehicleBinding

    private val viewModel: TenantsVehicleViewModel by viewModels()

    var adapter = VehiclesAdapter(::onItemClicked, ::onMenuItemClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsVehicleBinding.inflate(inflater, container, false)

        initCollectors()

        initVehicleRecycler()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getVehicleList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.vehicleUiState){
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

                    adapter.submitList(it.result?.data?.vehicles)

                    if (it.result?.data?.vehicles?.isEmpty() == false){

                        binding.emptyData.visibility = View.GONE

                    }else{

                        binding.emptyData.visibility = View.VISIBLE

                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.deleteVehicleUiState){
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
                    viewModel.getVehicleList()

                }
            }
        }

    }

    private fun initVehicleRecycler() {

        binding.recyclerVehicle.adapter = adapter

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.addImage.setOnClickListener{

            findNavController().navigate(R.id.action_tenantsVehicleFragment_to_addVehicleFragment)

        }
    }

    private fun onItemClicked(vehicle: GetVehicleResponse.Data.Vehicle) {

//        val bundle = Bundle()
//        bundle.putString(AppConsts.PROPERTY_ID, vehicle.id.toString())
//        findNavController().navigate(R.id.action_propertiesFragment_to_propertyDetailsFragment, bundle)

    }

    private fun onMenuItemClick(vehicle: GetVehicleResponse.Data.Vehicle, position: Int) {

        performOptionsMenuClick(vehicle, position)
    }

    private fun performOptionsMenuClick(vehicle: GetVehicleResponse.Data.Vehicle, position: Int) {

        val pop= PopupMenu(requireContext(), binding.recyclerVehicle.getChildAt(position))
        pop.inflate(R.menu.property_menu)

        pop.setOnMenuItemClickListener {item->

            when(item.itemId)

            {
                R.id.edit->{
                    editVehicle(vehicle)
                }

                R.id.delete->{

                    viewModel.deleteVehicleRequest.id = vehicle.id.toString()
                    viewModel.deleteVehicle()
                }


            }
            true
        }
        pop.show()

    }

    private fun editVehicle(property: GetVehicleResponse.Data.Vehicle) {

        val json = Gson().toJson(property).toString()
        val bundle = Bundle()
        bundle.putString(AppConsts.DETAIL_ARGUMENTS, json)
//        bundle.putInt(AppConsts.PROPERTY_ID, property.id)

        findNavController().navigate(R.id.action_tenantsVehicleFragment_to_updateVehicleFragment, bundle)

    }

}