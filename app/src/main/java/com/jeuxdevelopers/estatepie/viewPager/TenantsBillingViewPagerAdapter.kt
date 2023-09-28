package com.jeuxdevelopers.estatepie.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.billing.paid.TenantsPaidBillsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.billing.unpaid.TenantsUnPaidBillsFragment

class TenantsBillingViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {

        var getFragment: Fragment? = null
        when(position){
            0 -> getFragment = TenantsPaidBillsFragment()
            1 -> getFragment = TenantsUnPaidBillsFragment()
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