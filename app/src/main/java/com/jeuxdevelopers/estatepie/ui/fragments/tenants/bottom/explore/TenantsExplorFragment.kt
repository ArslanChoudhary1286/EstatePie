package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsExplorBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.explore.exploreads.adapters.AdsHorizontalAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.explore.exploreads.adapters.AdsVerticalAdapter
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TenantsExplorFragment : Fragment() {

    private lateinit var binding: FragmentTenantsExplorBinding

    private val viewModel: TenantsExplorViewModel by viewModels()

    var adapter = AdsVerticalAdapter(::onRecommendedItemClicked)

    var popularAdapter = AdsHorizontalAdapter(::onPopularItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsExplorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getRecommendedRecyclerData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    override fun onResume() {
        super.onResume()

        initCollectors()

        setAdapters()

    }

    private fun initClickListeners() {

        binding.btnViewAll.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsExploreFragment_to_tenantsViewAllFragment)
        }

        binding.heartImage.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsExploreFragment_to_tenantsFavoriteFragment)
        }

        binding.messageImage.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("data","tenants")
            findNavController().navigate(R.id.action_tenantsExploreFragment_to_inboxFragment2, bundle)
        }

        binding.notificationImage.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsExploreFragment_to_notificationFragment2)
        }

        binding.addImage.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsExploreFragment_to_addPropertyFragment2)
        }

        binding.navImage.setOnClickListener {

            (requireActivity() as ManagementDrawerListener).openDrawer()
        }

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

                    adapter.submitList(it.result?.data?.recommended)

                    if (it.result?.data?.popular?.size!! > 0)
                        popularAdapter.submitList(it.result.data.popular)
                    else
                        binding.noDataFound.visibility = View.VISIBLE

                }
            }
        }
    }

    private fun setAdapters() {

        binding.recyclerAdsPopular.adapter = popularAdapter

        binding.recyclerAdsRecommended.adapter = adapter

    }

    private fun getRecommendedRecyclerData() {

        viewModel.getRecommendedExploreList()
    }

    private fun onRecommendedItemClicked(item : ExploreRecommendedResponse.Data.Recommended){

        val bundle = Bundle()
        bundle.putString(AppConsts.PROPERTY_ID, item.id.toString())
//        bundle.putString(AppConsts.DETAIL_ARGUMENTS, Gson().toJson(item))
        findNavController().navigate(R.id.action_tenantsExploreFragment_to_tenantsAdsDetailFragment, bundle)

    }

    private fun onPopularItemClicked(item : ExploreRecommendedResponse.Data.Popular){

        val bundle = Bundle()
        bundle.putString(AppConsts.PROPERTY_ID, item.id.toString())
        findNavController().navigate(R.id.action_tenantsExploreFragment_to_tenantsAdsDetailFragment, bundle)
//        ToastUtility.errorToast(requireContext(), "you can check details after login.")

    }

}