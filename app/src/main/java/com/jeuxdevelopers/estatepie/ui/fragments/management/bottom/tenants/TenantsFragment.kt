package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsBinding
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.MyTenantsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.adapters.TenantsAdapter
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TenantsFragment : Fragment() {

    private lateinit var binding: FragmentTenantsBinding

    private val viewModel: TenantsViewModel  by viewModels()

    var adapter = TenantsAdapter(::onItemClicked)

    var status: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsBinding.inflate(inflater, container, false)

        initCollectors()

        initTenantsRecycler()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        viewModel.request.search = ""

        viewModel.getMyTenantsList()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.myTenantsUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    adapter.submitList(it.result?.data?.tenants)

                    status = it.result?.message.toString()

                }
            }
        }
    }

    private fun initTenantsRecycler() {

        binding.recyclerTenants.adapter = adapter

    }

    private fun initClickListeners() {

        binding.navImage.setOnClickListener {
            (requireActivity() as ManagementDrawerListener).openDrawer()
        }

        binding.notificationImage.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsFragment_to_notificationFragment)
        }

        binding.filterImage.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsFragment_to_filterFragment)
        }

        binding.etSearch.setOnEditorActionListener{ _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                getTenantRecyclerDataWithQuery(binding.etSearch.text.toString().trim())

            }
            true
        }

    }

    private fun getTenantRecyclerDataWithQuery(query: String) {

        viewModel.request.search = query
        viewModel.getMyTenantsList()

    }

    private fun onItemClicked(item : MyTenantsResponse.Data.Tenant){

        val bundle = Bundle()
        bundle.putString(AppConsts.PROFILE_ID, item.id.toString())

        if (status == "Get Tenants successfully"){

            findNavController().navigate(R.id.action_tenantsFragment_to_assignPropertyToTenantFragment, bundle)


        }else{
            findNavController().navigate(R.id.action_tenantsFragment_to_tenantsProfileFragment, bundle)
        }


    }

}