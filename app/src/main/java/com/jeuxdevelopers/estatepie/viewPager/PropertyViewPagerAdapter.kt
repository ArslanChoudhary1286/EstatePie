package com.jeuxdevelopers.estatepie.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.ads.AdsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.commercial.CommercialFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.ResidentialFragment

class PropertyViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {

        var getFragment: Fragment? = null
        when(position){
            0 -> getFragment = ResidentialFragment()
            1 -> getFragment = CommercialFragment()
            2 -> getFragment = AdsFragment()
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