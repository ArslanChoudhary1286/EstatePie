package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsDashboardBinding
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.viewPager.TenantDashPagerAdapter
import com.jeuxdevelopers.estatepie.viewPager.TenantsBillingViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TenantsDashboardFragment : Fragment() {

    private lateinit var binding: FragmentTenantsDashboardBinding

    private val viewModel: TenantsDashboardViewModel by viewModels()

//    private val navController: NavController by lazy {
//        requireActivity().findNavController(R.id.nav_host_fragment_content_tenants_dashboard)
//    }

    private lateinit var pagerAdapter: TenantDashPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsDashboardBinding.inflate(inflater, container, false)

        initCollectors()

        initPagerAdapter()

        initViewPager()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun initCollectors() {

        if (viewModel.getCurrentUser() != null) {

            binding.tvTitle.text = "Hi, " + viewModel.getCurrentUser()!!.details.first_name + " " + viewModel.getCurrentUser()!!.details.last_name
        }

//        collectLatestLifecycleFlow(viewModel.dashboardUiState) {
//            if (it) {
//
//
//            }
//
//        }
    }

    private fun initPagerAdapter() {

        pagerAdapter = TenantDashPagerAdapter(childFragmentManager)

    }

    private fun initViewPager() {

        binding.tenantDashboardViewPager.adapter = pagerAdapter
        binding.tenantDashboardViewPager.offscreenPageLimit = 3

        binding.tenantDashboardViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {

                if (position == 0){
                    setMaintenanceRequestsBtnViews()
                } else if (position == 1){
                    setUpcomingBillsBtnViews()
                }else{

                    setIncidentReportsBtnViews()

                }

            }
        })

    }

    private fun initClickListeners() {

        binding.maintenanceRequests.setOnClickListener{

            setMaintenanceRequestsBtnViews()
            binding.tenantDashboardViewPager.currentItem = 0
//            navController.navigate(R.id.maintenanceTenantsFragment)

        }
        binding.upcomingBills.setOnClickListener{

            setUpcomingBillsBtnViews()
            binding.tenantDashboardViewPager.currentItem = 1
//            navController.navigate(R.id.upcomingBillsFragment)

        }
        binding.incidentReports.setOnClickListener{

            setIncidentReportsBtnViews()
            binding.tenantDashboardViewPager.currentItem = 2
//            navController.navigate(R.id.incidentTenantsFragment)

        }
        binding.messageImage.setOnClickListener{

            val bundle = Bundle()
            bundle.putString("data","tenants")
            findNavController().navigate(R.id.action_tenantsDashboardFragment_to_inboxFragment2, bundle)

        }

        binding.notificationImage.setOnClickListener{

            findNavController().navigate(R.id.action_tenantsDashboardFragment_to_notificationFragment2)
        }

        binding.navImage.setOnClickListener {

            (requireActivity() as ManagementDrawerListener).openDrawer()
        }

    }

    private fun setMaintenanceRequestsBtnViews(){

        binding.maintenanceRequests.setBackgroundResource(R.drawable.ic_ripple_blue_12)
        binding.upcomingBills.setBackgroundResource(R.drawable.ic_ripple_white_12)
        binding.incidentReports.setBackgroundResource(R.drawable.ic_ripple_white_12)

    }
    private fun setUpcomingBillsBtnViews(){

        binding.maintenanceRequests.setBackgroundResource(R.drawable.ic_ripple_white_12)
        binding.upcomingBills.setBackgroundResource(R.drawable.ic_ripple_blue_12)
        binding.incidentReports.setBackgroundResource(R.drawable.ic_ripple_white_12)

    }
    private fun setIncidentReportsBtnViews(){

        binding.maintenanceRequests.setBackgroundResource(R.drawable.ic_ripple_white_12)
        binding.upcomingBills.setBackgroundResource(R.drawable.ic_ripple_white_12)
        binding.incidentReports.setBackgroundResource(R.drawable.ic_ripple_blue_12)

    }
}