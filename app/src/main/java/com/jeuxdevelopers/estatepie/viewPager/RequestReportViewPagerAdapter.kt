package com.jeuxdevelopers.estatepie.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.request.TenantsRequestFragment
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.request.incident.TenanstsIncidentReportFragment
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.request.maintenance.TenantsMaintenanceRequestFragment

class RequestReportViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {

        var getFragment: Fragment? = null
        when(position){
            0 -> getFragment = TenantsMaintenanceRequestFragment()
            1 -> getFragment = TenanstsIncidentReportFragment()
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