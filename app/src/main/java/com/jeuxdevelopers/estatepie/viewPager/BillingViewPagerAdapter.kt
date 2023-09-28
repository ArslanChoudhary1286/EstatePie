package com.jeuxdevelopers.estatepie.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.paid.PaidBillsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.unpaid.UnPaidFragment

class BillingViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {

        var getFragment: Fragment? = null
        when(position){
            0 -> getFragment = PaidBillsFragment()
            1 -> getFragment = UnPaidFragment()
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