package com.jeuxdevelopers.estatepie.ui.fragments.management.pager

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentManagementPagerBinding
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.viewPager.MainManagementPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementPagerFragment : Fragment() {

    private lateinit var binding: FragmentManagementPagerBinding

    private val viewModel: ManagementPagerViewModel by viewModels()

    private lateinit var pagerAdapter: MainManagementPagerAdapter

    private var mView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagementPagerBinding.inflate(inflater, container, false)
        mView = binding.root


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPagerAdapter()

        initViewPager()

        binding.bottomNav.setOnItemSelectedListener{ item: MenuItem ->
            when (item.itemId) {
                R.id.managementDashboardFragment -> {
                    binding.mainPager.currentItem = 0
                }
                R.id.propertiesFragment -> {
                    binding.mainPager.currentItem = 1
                }
                R.id.tenantsFragment -> {
                    binding.mainPager.currentItem = 2
                }
                R.id.billingFragment -> {
                    binding.mainPager.currentItem = 3
                }
                R.id.requestsFragment -> {
                    binding.mainPager.currentItem = 4
                }
            }
            true
        }


    }

    private fun initPagerAdapter() {

        pagerAdapter = MainManagementPagerAdapter(childFragmentManager)

    }

    private fun initViewPager() {

        binding.mainPager.adapter = pagerAdapter
        binding.mainPager.offscreenPageLimit = 5

        binding.mainPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {

                when(position){
                    0 -> binding.bottomNav.selectedItemId = R.id.managementDashboardFragment
                    1 -> binding.bottomNav.selectedItemId = R.id.propertiesFragment
                    2 -> binding.bottomNav.selectedItemId = R.id.tenantsFragment
                    3 -> binding.bottomNav.selectedItemId = R.id.billingFragment
                    4 -> binding.bottomNav.selectedItemId = R.id.requestsFragment

                }

            }
        })

    }

    override fun onResume() {
        if (binding.mainPager.adapter != null) {
            val selection = binding.mainPager.currentItem
            //Log.d(TAG, "onResume: Current selection -> ${selection}")
            binding.mainPager.currentItem = selection
        }
        super.onResume()
    }

    private fun hideBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(view.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }
            })
    }

    private fun showBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.VISIBLE
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mView?.let {
            val parentViewGroup = it.parent as? ViewGroup
            parentViewGroup?.removeAllViews()
        }
        mView = null
    }
}