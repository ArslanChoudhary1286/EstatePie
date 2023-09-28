package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.maintainrequest.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.jeuxdevelopers.estatepie.databinding.FragmentMaintenanceTenantsDetailBinding
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.RequestDetailByIdResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports.requestsByProperty.detail.adapter.ImagesReportsDetailAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MaintenanceTenantsDetailFragment : Fragment() {

    private lateinit var binding: FragmentMaintenanceTenantsDetailBinding

    private val viewModel: MaintenanceTenantsDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMaintenanceTenantsDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCollectors()

        setBundleData()

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.requestsDetailUiState){
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

                    updateViews(it.result?.data)

                }
            }
        }

    }

    private fun updateViews(data: RequestDetailByIdResponse.Data?) {

        data.let {

            binding.userName.text = viewModel.getCurrentUser()?.name
            binding.requestStatus.text = it?.status
            binding.requestDate.text = it?.created_at
            binding.requestName.text = it?.type
            binding.propertyName.text = it?.title
            binding.etDescription.setText(it?.description)

            if (it?.images?.isNotEmpty() == true){

                initImagesRecycler(it.images)
                binding.noPopularDataFound.visibility = View.GONE

            }else{
                binding.noPopularDataFound.visibility = View.VISIBLE
            }

        }
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

    }
    private fun setBundleData() {

        val json = arguments?.getString(AppConsts.INTENT_ID).toString()

        viewModel.getRequestByPropertyId(json.toInt())

    }

    private fun initImagesRecycler(propertyImages: List<RequestDetailByIdResponse.Data.Image>?) {

        val adapter = ImagesReportsDetailAdapter()
        adapter.submitList(propertyImages)
        binding.recyclerImages.adapter = adapter
        binding.arIndicator.attachTo(binding.recyclerImages, true)
        val snapHelper = PagerSnapHelper()
        binding.recyclerImages.onFlingListener = null
        snapHelper.attachToRecyclerView(binding.recyclerImages)

    }

}