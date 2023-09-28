package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentRequestsBinding
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.viewPager.ManageRequestViewPagerAdapter
import com.jeuxdevelopers.estatepie.viewPager.PropertyViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestsFragment : Fragment() {

    private lateinit var binding: FragmentRequestsBinding

    private val viewModel: RequestsViewModel by viewModels()

    private lateinit var pagerAdapter: ManageRequestViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestsBinding.inflate(inflater, container, false)

        initPagerAdapter()

        initViewPager()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initPagerAdapter() {

        pagerAdapter = ManageRequestViewPagerAdapter(childFragmentManager)

    }

    private fun initViewPager() {

        binding.requestViewPager.adapter = pagerAdapter
        binding.requestViewPager.offscreenPageLimit = 3

        binding.requestViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {

                when(position){
                    0 -> binding.requestBtn.isChecked = true
                    1 -> binding.incidentBtn.isChecked = true
                    2 -> binding.eventsBtn.isChecked = true
                }

            }
        })

    }

    private fun initClickListeners() {

        binding.addImage.setOnClickListener {
            findNavController().navigate(R.id.action_requestsFragment_to_createEventFragment)
        }

        binding.radioLayout.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->

            val radio: RadioButton = group.findViewById(checkedId)
            if (radio.text.toString() == getString(R.string.request_maintenance)) {

                binding.addImage.visibility = View.GONE
                binding.linearLayout.setPadding(0, 0, 0, 0)
//                navController.navigate(R.id.maintenanceRequestsFragment)
                binding.requestViewPager.currentItem = 0

            } else if (radio.text.toString() == getString(R.string.incident_nreports)) {

                binding.addImage.visibility = View.GONE
                binding.linearLayout.setPadding(0, 0, 0, 0)
//                navController.navigate(R.id.incidentReportsFragment)
                binding.requestViewPager.currentItem = 1

            } else {

                binding.addImage.visibility = View.VISIBLE
                binding.linearLayout.setPadding(0, 0, 42, 0)
//                navController.navigate(R.id.eventUpdatesFragment)
                binding.requestViewPager.currentItem = 2
            }

        })


        binding.navImage.setOnClickListener {
            (requireActivity() as ManagementDrawerListener).openDrawer()
        }

        binding.notificationImage.setOnClickListener{
            findNavController().navigate(R.id.action_requestsFragment_to_notificationFragment)
        }
    }

}