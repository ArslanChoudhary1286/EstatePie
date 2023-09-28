package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.exploreads

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentManagementExploreAdsBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.exploreads.adapter.ManagementAdsHorizontalAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.exploreads.adapter.ManagementAdsVerticalAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ManagementExploreAdsFragment : Fragment() {

    private lateinit var binding: FragmentManagementExploreAdsBinding

    private val viewModel: ManagementExploreAdsViewModel by viewModels()

    private var recommendedAdapter = ManagementAdsVerticalAdapter(::onRecommendedItemClicked)

    private var popularAdapter = ManagementAdsHorizontalAdapter(::onPopularItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagementExploreAdsBinding.inflate(inflater, container, false)

        initPopularsRecycler()

        initRecommendedRecycler()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRecommendedExploreList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCollectors()

        initClickListeners()

    }
    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.exploreUiState){
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

                    recommendedAdapter.submitList(it.result?.data?.recommended)

                    if (it.result?.data?.popular?.size!! > 0){

                        binding.ivNoData.visibility = View.GONE
                        popularAdapter.submitList(it.result.data.popular)

                    } else{

                        binding.ivNoData.visibility = View.VISIBLE

                    }

                }
            }
        }
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { requireActivity().onBackPressed() }

        binding.btnViewAll.setOnClickListener{

            findNavController().navigate(R.id.action_managementExploreAdsFragment_to_managementViewAllFragment)

        }

    }

    private fun initRecommendedRecycler() {

        binding.recyclerAdsRecommended.adapter = recommendedAdapter

    }

    private fun initPopularsRecycler() {

        binding.recyclerAdsPopular.adapter = popularAdapter

    }

    private fun onPopularItemClicked(item : ExploreRecommendedResponse.Data.Popular){

        val bundle = Bundle()
        bundle.putString(AppConsts.PROPERTY_ID, item.id.toString())
        findNavController().navigate(R.id.action_managementExploreAdsFragment_to_managementAdDetailFragment, bundle)

    }

    private fun onRecommendedItemClicked(item : ExploreRecommendedResponse.Data.Recommended){

        val bundle = Bundle()
        bundle.putString(AppConsts.PROPERTY_ID, item.id.toString())
        findNavController().navigate(R.id.action_managementExploreAdsFragment_to_managementAdDetailFragment, bundle)

    }

}