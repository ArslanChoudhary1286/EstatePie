package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard

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
import com.jeuxdevelopers.estatepie.databinding.FragmentManagementDashboardBinding
import com.jeuxdevelopers.estatepie.models.management.ManagementDashboardModel
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.rentfees.adapter.ManagementDashboardAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.adapters.ResidentialAdapter
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.viewPager.ManagementDashPagerAdapter
import com.jeuxdevelopers.estatepie.viewPager.TenantDashPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementDashboardFragment : Fragment() {

    private lateinit var binding:FragmentManagementDashboardBinding

    private val viewModel: ManagementDashboardViewModel by viewModels()

    private lateinit var pagerAdapter: ManagementDashPagerAdapter

    var adapter = ManagementDashboardAdapter(::onItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagementDashboardBinding.inflate(inflater,container,false)

        initCollectors()

        initPagerAdapter()

        initViewPager()

        initRecycler()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        loadRecyclerData()

    }

    private fun loadRecyclerData() {

        viewModel.list.add(ManagementDashboardModel(getString(R.string.rent_fees),
            resources.getDrawable(R.drawable.ic_rent)))
        viewModel.list.add(ManagementDashboardModel(getString(R.string.all_reports),
            resources.getDrawable(R.drawable.ic_reports)))
        viewModel.list.add(ManagementDashboardModel(getString(R.string.utility_bills_payments),
            resources.getDrawable(R.drawable.ic_util_bill)))
        viewModel.list.add(ManagementDashboardModel(getString(R.string.my_tenants),
            resources.getDrawable(R.drawable.ic_rent)))
        viewModel.list.add(ManagementDashboardModel(getString(R.string.broadcast_messages),
            resources.getDrawable(R.drawable.ic_message)))

        adapter.submitList(viewModel.list)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initRecycler() {

//        binding.linearLayout.adapter = adapter

    }

    @SuppressLint("SetTextI18n")
    private fun initCollectors() {

        if (viewModel.getCurrentUser() != null) {
            binding.tvTitle.text = "Hi, " + viewModel.getCurrentUser()!!.details.business_name
        }

    }

    private fun initPagerAdapter() {

        pagerAdapter = ManagementDashPagerAdapter(childFragmentManager)

    }

    private fun initViewPager() {

        binding.managementDashboardViewPager.adapter = pagerAdapter
        binding.managementDashboardViewPager.offscreenPageLimit = 3

        binding.managementDashboardViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {

//                binding.linearLayout.smoothScrollToPosition(position)
//                adapter.updateView(position, requireContext())

                if (position == 0){
                    setRentFeesBtnViews()
                } else if (position == 1){
                    setAllReportsBtnViews()
                }else{

                    setBillsPaymentBtnViews()

                }

            }
        })

    }

    private fun initClickListeners() {

        binding.rentsFees.setOnClickListener{
            setRentFeesBtnViews()
            binding.managementDashboardViewPager.currentItem = 0
//            navController.navigate(R.id.rentFeesFragment)
        }
        binding.allReports.setOnClickListener{
            setAllReportsBtnViews()
            binding.managementDashboardViewPager.currentItem = 1

//            navController.navigate(R.id.allReportsFragment)
        }
        binding.billsPayment.setOnClickListener{
            setBillsPaymentBtnViews()
            binding.managementDashboardViewPager.currentItem = 2

//            navController.navigate(R.id.billsPaymentFragment)
        }
        binding.messageImage.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("data","management")
            findNavController().navigate(R.id.action_managementDashboardFragment_to_inboxFragment, bundle)
        }
        binding.notificationImage.setOnClickListener{
            findNavController().navigate(R.id.action_managementDashboardFragment_to_notificationFragment)
        }

        binding.navImage.setOnClickListener {
            (requireActivity() as ManagementDrawerListener).openDrawer()
        }

    }

    private fun setRentFeesBtnViews(){

        binding.rentsFees.setBackgroundResource(R.drawable.ic_ripple_blue_12)
        binding.allReports.setBackgroundResource(R.drawable.ic_ripple_white_12)
        binding.billsPayment.setBackgroundResource(R.drawable.ic_ripple_white_12)

    }
    private fun setAllReportsBtnViews(){

        binding.rentsFees.setBackgroundResource(R.drawable.ic_ripple_white_12)
        binding.allReports.setBackgroundResource(R.drawable.ic_ripple_blue_12)
        binding.billsPayment.setBackgroundResource(R.drawable.ic_ripple_white_12)

    }
    private fun setBillsPaymentBtnViews(){

        binding.rentsFees.setBackgroundResource(R.drawable.ic_ripple_white_12)
        binding.allReports.setBackgroundResource(R.drawable.ic_ripple_white_12)
        binding.billsPayment.setBackgroundResource(R.drawable.ic_ripple_blue_12)

    }

    private fun onItemClicked(item: ManagementDashboardModel, position: Int) {

        binding.managementDashboardViewPager.currentItem = position

        when(position){
            0 ->{
            }1 -> {
            }2 -> {
            }3 -> {
            }4 -> {
            }
        }


    }

}