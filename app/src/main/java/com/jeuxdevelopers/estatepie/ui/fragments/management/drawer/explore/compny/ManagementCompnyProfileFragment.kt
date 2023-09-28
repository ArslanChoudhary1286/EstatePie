package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.compny

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentManagementCompnyProfileBinding
import com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.exploreads.adapter.ManagementAdsVerticalAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementCompnyProfileFragment : Fragment() {

    private lateinit var binding: FragmentManagementCompnyProfileBinding
    private lateinit var viewModel: ManagementCompnyProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagementCompnyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        initPropertyAdsRecycler()
    }

    private fun initClickListeners() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }


    private fun initPropertyAdsRecycler() {
        val adapter = ManagementAdsVerticalAdapter {
            findNavController().navigate(R.id.action_managementCompnyProfileFragment_to_managementAdDetailFragment)
        }
//        adapter.submitList(mutableListOf("1", "2", "3", "4", "5", "6", "7", "8"))
        binding.recyclerPropertyAds.adapter = adapter
    }

}