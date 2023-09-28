package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing

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
import com.google.android.material.tabs.TabLayout
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentBillingBinding
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.paid.PaidBillsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.unpaid.UnPaidFragment
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.viewPager.BillingViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding

    private val viewModel: BillingViewModel by viewModels()

//    private val navController: NavController by lazy {
//        requireActivity().findNavController(R.id.nav_host_fragment_content_billing)
//    }

    private lateinit var pagerAdapter: BillingViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater, container, false)

        initPagerAdapter()

        initViewPager()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initPagerAdapter() {

        pagerAdapter = BillingViewPagerAdapter(childFragmentManager)

    }

    private fun initViewPager() {

        binding.billingViewPager.adapter = pagerAdapter
        binding.billingViewPager.offscreenPageLimit = 2

        binding.billingViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {

                if (position == 0){
                    binding.paidBtn.isChecked = true
                } else{
                    binding.unPaidBtn.isChecked = true
                }

            }
        })

    }

    private fun initClickListeners() {

        binding.addImage.setOnClickListener {

            findNavController().navigate(R.id.action_billingFragment_to_sendInvoiceFragment)

        }

        binding.navImage.setOnClickListener {

            (requireActivity() as ManagementDrawerListener).openDrawer()

        }

        binding.notificationImage.setOnClickListener{

            findNavController().navigate(R.id.action_billingFragment_to_notificationFragment)

        }

        binding.radioLayout.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.paid_btn -> {

                    binding.addImage.visibility = View.GONE
                    binding.linearLayout.setPadding(0, 0, 0, 0)

                    binding.billingViewPager.currentItem = 0

                }

                else -> {

                    binding.addImage.visibility = View.VISIBLE
                    binding.linearLayout.setPadding(0, 0, 42, 0)

                    binding.billingViewPager.currentItem = 1

                }
            }
        }

    }

}