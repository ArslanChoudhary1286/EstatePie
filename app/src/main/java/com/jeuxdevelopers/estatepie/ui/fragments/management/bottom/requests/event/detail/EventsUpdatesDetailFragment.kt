package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.event.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.jeuxdevelopers.estatepie.databinding.FragmentEventsUpdatesDetailBinding
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.EventsDetailResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.event.detail.adapter.EventsDetailAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EventsUpdatesDetailFragment : Fragment() {

    private lateinit var binding: FragmentEventsUpdatesDetailBinding

    private val viewModel: EventsUpdatesDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsUpdatesDetailBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun updateViews(data: EventsDetailResponse.Data?) {

        data.let {

            binding.tvUserName.text = data?.title
            binding.tvStatus.text = data?.date
            binding.tvEventType.text = data?.event_type
            binding.tvDate.text = data?.date
            binding.tvTime.text = data?.time
            if (data?.property_name?.isNotEmpty() == true)
                binding.tvPropertyName.text = data?.property_name
            else
                binding.tvPropertyName.text = "N/A"
            binding.tvAddress.text = data?.address

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
        viewModel.request.id = json

        viewModel.getEventsDetail()

    }

    private fun initImagesRecycler(propertyImages: List<EventsDetailResponse.Data.Image>?) {

        val adapter = EventsDetailAdapter()
        adapter.submitList(propertyImages)
        binding.recyclerImages.adapter = adapter
        binding.arIndicator.attachTo(binding.recyclerImages, true)
        val snapHelper = PagerSnapHelper()
        binding.recyclerImages.onFlingListener = null
        snapHelper.attachToRecyclerView(binding.recyclerImages)

    }

}