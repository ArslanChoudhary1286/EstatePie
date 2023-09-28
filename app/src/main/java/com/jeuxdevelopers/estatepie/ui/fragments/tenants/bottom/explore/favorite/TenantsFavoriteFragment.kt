package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsFavoriteBinding
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.FavoritesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore.adapter.FavoriteAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TenantsFavoriteFragment : Fragment() {


    private lateinit var binding: FragmentTenantsFavoriteBinding
    private val viewModel: TenantsFavoriteViewModel by viewModels()
    var adapter = FavoriteAdapter(::onFavoriteItemClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFavoriteRecycler()
        initCollectors()
        initClickListeners()
        getFavorites()

    }

    private fun initCollectors() {
        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.favoritesUiState){
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

                }
            }
        }
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }
    }

    private fun initFavoriteRecycler() {
//        val adapter = FavoriteAdapter {
//            findNavController().navigate(R.id.action_tenantsFavoriteFragment_to_tenantsAdsDetailFragment)
//        }
//        adapter.submitList(mutableListOf("1", "2", "3", "4", "5", "6", "7", "8"))
        binding.recyclerFavorite.adapter = adapter
    }

    private fun getFavorites() {

        viewModel.getFavorites()
    }

    private fun onFavoriteItemClick(item : FavoritesResponse.Data.Recommended){

        val bundle = Bundle()
//        bundle.putString(AppConsts.DETAIL_ARGUMENTS, Gson().toJson(item))
        bundle.putString(AppConsts.PROPERTY_ID, item.id.toString())
        findNavController().navigate(R.id.action_tenantsFavoriteFragment_to_tenantsAdsDetailFragment, bundle)

    }

}