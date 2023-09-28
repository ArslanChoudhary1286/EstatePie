package com.jeuxdevelopers.estatepie.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports.AllReportsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.billspayment.BillsPaymentFragment
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.rentfees.RentFeesFragment
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.incidentreport.IncidentTenantsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.maintainrequest.MaintenanceTenantsFragment
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.upcomingbills.UpcomingBillsFragment

class ManagementDashPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {

        var getFragment: Fragment? = null
        when(position){
            0 -> getFragment = RentFeesFragment()
            1 -> getFragment = AllReportsFragment()
            2 -> getFragment = BillsPaymentFragment()
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