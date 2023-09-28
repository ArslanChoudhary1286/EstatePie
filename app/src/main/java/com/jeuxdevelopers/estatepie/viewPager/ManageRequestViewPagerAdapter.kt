package com.jeuxdevelopers.estatepie.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.event.EventUpdatesFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.incident.IncidentReportsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.maintenance.MaintenanceRequestsFragment

class ManageRequestViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {

        var getFragment: Fragment? = null
        when(position){
            0 -> getFragment = MaintenanceRequestsFragment()
            1 -> getFragment = IncidentReportsFragment()
            2 -> getFragment = EventUpdatesFragment()
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