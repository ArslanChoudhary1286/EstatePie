package com.jeuxdevelopers.estatepie.viewPager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.BillingFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.ManagementDashboardFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.PropertiesFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.RequestsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.TenantsFragment

class ManagementPagerAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int  = 5

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ManagementDashboardFragment()
            1 -> PropertiesFragment()
            3 -> TenantsFragment()
            4 -> BillingFragment()
            else -> RequestsFragment()
        }
    }

}