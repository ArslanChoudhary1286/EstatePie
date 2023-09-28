package com.jeuxdevelopers.estatepie.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.incidentreport.IncidentTenantsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.maintainrequest.MaintenanceTenantsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.upcomingbills.UpcomingBillsFragment

class TenantDashPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {

        var getFragment: Fragment? = null
        when(position){
            0 -> getFragment = MaintenanceTenantsFragment()
            1 -> getFragment = UpcomingBillsFragment()
            2 -> getFragment = IncidentTenantsFragment()
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