package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.maintenance.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentMaintenanceRequestDetailBinding
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.RequestDetailByIdResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.RequestsByPropertyIdResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.MaintenanceWithFilterDetailResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.PostCommentResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports.requestsByProperty.detail.adapter.ImagesReportsDetailAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.maintenance.detail.adapter.MaintenanceWithFilterDetailAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MaintenanceRequestDetailFragment : Fragment() {

    private lateinit var binding: FragmentMaintenanceRequestDetailBinding

    private val viewModel: MaintenanceRequestDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMaintenanceRequestDetailBinding.inflate(inflater, container, false)

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

    override fun onResume() {
        super.onResume()

        initCollectors()

    }

    private fun initAdapter() {

        val azAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter, R.id.adapterTxtView, resources.getStringArray(R.array.maintenance_reports)
        )
        binding.etChangeStatus.setAdapter(azAdapter)

        binding.etChangeStatus.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                viewModel.changeRequestStatusRequest.id = viewModel.request.id
                viewModel.changeRequestStatusRequest.status = position.toString()

                viewModel.changeRequestStatus()

        }

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

        collectLatestLifecycleFlow(viewModel.changeStatusUiState){
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

                    ToastUtility.successToast(requireContext(), it.result?.message)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.postCommentUiState){
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

                    updateCommentView(it.result?.data)

                }
            }
        }

    }

    private fun updateCommentView(data: PostCommentResponse.Data?) {

        binding.etComment.text = data?.comments
        binding.editComment.visibility = View.VISIBLE
        binding.constraintLayout.visibility = View.GONE
    }

    private fun updateViews(data: MaintenanceWithFilterDetailResponse.Data?) {

        data.let {

            binding.userName.text = viewModel.getCurrentUser()?.name
            if (it?.is_complete == 0)
            binding.etChangeStatus.setText("In Progress")
            else
                binding.etChangeStatus.setText("Completed")

            binding.requestDate.text = it?.created_at
            binding.requestName.text = it?.title
            binding.etDescription.setText(it?.description)

            if (it?.comments?.isNotEmpty() == true){

                binding.etComment.text = it.comments[0].comments
                binding.editComment.visibility = View.VISIBLE
                binding.constraintLayout.visibility = View.GONE

            }else{
                binding.editComment.visibility = View.GONE
                binding.constraintLayout.visibility = View.VISIBLE
            }


            if (it?.images?.isNotEmpty() == true){

                initImagesRecycler(it.images)
                binding.noPopularDataFound.visibility = View.GONE

            }else{
                binding.noPopularDataFound.visibility = View.VISIBLE
            }

            initAdapter()

        }
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.sendImg.setOnClickListener {

            viewModel.postCommentRequest.request_id = viewModel.request.id
            viewModel.postCommentRequest.comments = binding.etSendComment.text.toString()

            viewModel.postComment()

            binding.etSendComment.text = null
        }

        binding.editComment.setOnClickListener {
            binding.constraintLayout.visibility = View.VISIBLE
            binding.editComment.visibility = View.GONE
        }

    }

    private fun setBundleData() {

        val json = arguments?.getString(AppConsts.INTENT_ID).toString()
        viewModel.request.id = json

        viewModel.getMaintenanceWithFilterDetail()

    }

    private fun initImagesRecycler(propertyImages: List<MaintenanceWithFilterDetailResponse.Data.Image>?) {

        if(binding.recyclerImages.onFlingListener == null){
            val adapter = MaintenanceWithFilterDetailAdapter()
            adapter.submitList(propertyImages)
            binding.recyclerImages.adapter = adapter
            binding.arIndicator.attachTo(binding.recyclerImages, true)
            val snapHelper = PagerSnapHelper()
            binding.recyclerImages.onFlingListener = null
            snapHelper.attachToRecyclerView(binding.recyclerImages)

        }

    }

}