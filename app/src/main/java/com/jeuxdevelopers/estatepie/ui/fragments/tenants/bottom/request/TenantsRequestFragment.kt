package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.request

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsRequestBinding
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.viewPager.BillingViewPagerAdapter
import com.jeuxdevelopers.estatepie.viewPager.RequestReportViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TenantsRequestFragment : Fragment() {

    private lateinit var binding: FragmentTenantsRequestBinding

    private val viewModel: TenantsRequestViewModel by viewModels()

    private lateinit var pagerAdapter: RequestReportViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsRequestBinding.inflate(inflater, container, false)

        initPagerAdapter()

        initViewPager()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initClickListeners() {

        binding.addImage.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsRequestFragment_to_tenantsAddReportsFragment)
        }

        binding.notificationImage.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsRequestFragment_to_notificationFragment2)
        }

        binding.navImage.setOnClickListener {
            (requireActivity() as ManagementDrawerListener).openDrawer()
        }

        binding.radioLayout.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.maintenance_btn -> {

//                    binding.addImage.visibility = View.GONE
//                    binding.linearLayout.setPadding(0, 0, 0, 0)
//
                    binding.requestViewPager.currentItem = 0

                }

                else -> {

//                    binding.addImage.visibility = View.VISIBLE
//                    binding.linearLayout.setPadding(0, 0, 42, 0)

                    binding.requestViewPager.currentItem = 1

                }
            }
        }

    }


    private fun initPagerAdapter() {

        pagerAdapter = RequestReportViewPagerAdapter(childFragmentManager)

    }

    private fun initViewPager() {

        binding.requestViewPager.adapter = pagerAdapter
        binding.requestViewPager.offscreenPageLimit = 2

        binding.requestViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {

                if (position == 0){
                    binding.maintenanceBtn.isChecked = true
                } else{
                    binding.incidentBtn.isChecked = true
                }

            }
        })

    }


}