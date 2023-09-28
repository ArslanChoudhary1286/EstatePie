package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.billing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsBillingBinding
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.viewPager.BillingViewPagerAdapter
import com.jeuxdevelopers.estatepie.viewPager.TenantsBillingViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TenantsBillingFragment : Fragment() {

    private lateinit var binding: FragmentTenantsBillingBinding

    private lateinit var viewModel: TenantsBillingViewModel

    private lateinit var pagerAdapter: TenantsBillingViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsBillingBinding.inflate(inflater, container, false)

        initPagerAdapter()

        initViewPager()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initClickListeners() {

        binding.radioLayout.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.paid_btn -> {

//                    binding.addImage.visibility = View.GONE
//                    binding.linearLayout.setPadding(0, 0, 0, 0)

                    binding.billingViewPager.currentItem = 0

                }

                else -> {

//                    binding.addImage.visibility = View.VISIBLE
//                    binding.linearLayout.setPadding(0, 0, 42, 0)

                    binding.billingViewPager.currentItem = 1

                }
            }
        }

        binding.navImage.setOnClickListener {
            (requireActivity() as ManagementDrawerListener).openDrawer()
        }

    }

    private fun initPagerAdapter() {

        pagerAdapter = TenantsBillingViewPagerAdapter(childFragmentManager)

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

}