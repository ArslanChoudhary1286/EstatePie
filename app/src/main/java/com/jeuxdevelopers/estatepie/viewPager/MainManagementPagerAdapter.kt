package com.jeuxdevelopers.estatepie.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.BillingFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.ManagementDashboardFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.PropertiesFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.ads.AdsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.commercial.CommercialFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.ResidentialFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.RequestsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.TenantsFragment

class MainManagementPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 5

    override fun getItem(position: Int): Fragment {

        var getFragment: Fragment? = null
        when(position){
            0 -> getFragment = ManagementDashboardFragment()
            1 -> getFragment = PropertiesFragment()
            2 -> getFragment = TenantsFragment()
            3 -> getFragment = BillingFragment()
            4 -> getFragment = RequestsFragment()
        }

        return getFragment!!
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//
//        var title: String? = null
//        when(position){
//            0 -> title = "Paid Bills"
//            1 -> title = "UnPaid Bills"
//        }
//        return title
//    }
}