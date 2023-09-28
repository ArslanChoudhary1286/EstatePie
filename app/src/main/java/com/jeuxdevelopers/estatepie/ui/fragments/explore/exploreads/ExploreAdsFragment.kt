package com.jeuxdevelopers.estatepie.ui.fragments.explore.exploreads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentExploreAdsBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.explore.exploreads.adapters.AdsHorizontalAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.explore.exploreads.adapters.AdsVerticalAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ExploreAdsFragment : Fragment() {

    private lateinit var binding: FragmentExploreAdsBinding

    private val viewModel: ExploreAdsViewModel by viewModels()

    private var popularAdapter = AdsHorizontalAdapter(::onPopularItemClicked)

    private var recommendedAdapter = AdsVerticalAdapter(::onRecommendedItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreAdsBinding.inflate(inflater, container, false)

        initPopularsRecycler()

        initRecommendedRecycler()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel.request.lat = "31.520370"
//        viewModel.request.lng = "74.358749"
//        viewModel.request.search = ""

        viewModel.getRecommendedExploreList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCollectors()

        initClickListeners()

    }

    private fun initPopularsRecycler() {

        binding.recyclerAdsPopular.adapter = popularAdapter

    }

    private fun initRecommendedRecycler() {

        binding.recyclerAdsRecommended.adapter = recommendedAdapter

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

        binding.addImage.setOnClickListener{

            findNavController().navigate(R.id.action_exploreAdsFragment_to_addPropertyFragment)

        }

        binding.btnViewAll.setOnClickListener{

            findNavController().navigate(R.id.action_exploreAdsFragment_to_viewAllFragment)

        }

    }

    private fun onPopularItemClicked(item : ExploreRecommendedResponse.Data.Popular){

        val bundle = Bundle()
        bundle.putString(AppConsts.DETAIL_ARGUMENTS, Gson().toJson(item))
        findNavController().navigate(R.id.action_exploreAdsFragment_to_adDetailFragment, bundle)

    }

    private fun onRecommendedItemClicked(item : ExploreRecommendedResponse.Data.Recommended){

        val bundle = Bundle()
        bundle.putString(AppConsts.DETAIL_ARGUMENTS, Gson().toJson(item))
        findNavController().navigate(R.id.action_exploreAdsFragment_to_adDetailFragment, bundle)

    }

}