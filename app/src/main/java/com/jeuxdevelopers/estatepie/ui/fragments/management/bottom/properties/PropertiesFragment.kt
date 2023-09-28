package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties

import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentPropertiesBinding
import com.jeuxdevelopers.estatepie.models.shared.SharedViewModel
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.ResidentialFragment
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.viewPager.PropertyViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PropertiesFragment : Fragment() {

    private lateinit var binding: FragmentPropertiesBinding

    private val viewModel: PropertiesViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var pagerAdapter: PropertyViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertiesBinding.inflate(inflater, container, false)

        initPagerAdapter()

        initViewPager()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initPagerAdapter() {

        pagerAdapter = PropertyViewPagerAdapter(childFragmentManager)

    }

    private fun initViewPager() {

        binding.propertyViewPager.adapter = pagerAdapter
        binding.propertyViewPager.offscreenPageLimit = 3

        binding.propertyViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {

                when(position){
                    0 -> binding.residentialBtn.isChecked = true
                    1 -> binding.commercialBtn.isChecked = true
                    2 -> binding.adsBtn.isChecked = true
                }

            }
        })

    }

    private fun initClickListeners() {

        binding.addImage.setOnClickListener {

            when (viewModel.intentId) {
                1 -> findNavController().navigate(R.id.action_propertiesFragment_to_addResidentialFragment)
                2 -> findNavController().navigate(R.id.action_propertiesFragment_to_addCommercialFragment)
                3 -> findNavController().navigate(R.id.action_propertiesFragment_to_addAdsFragment)
            }

        }

        binding.radioLayout.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.residentialBtn -> {

                    viewModel.intentId = 1
                    binding.propertyViewPager.currentItem = 0
                }

                R.id.commercialBtn -> {

                    viewModel.intentId = 2
                    binding.propertyViewPager.currentItem = 1

                }

                R.id.adsBtn ->{

                    viewModel.intentId = 3
                    binding.propertyViewPager.currentItem = 2

                }

            }
            binding.etSearch.text = null

        }

        binding.navImage.setOnClickListener {
            (requireActivity() as ManagementDrawerListener).openDrawer()
        }

        binding.notificationImage.setOnClickListener{
            findNavController().navigate(R.id.action_propertiesFragment_to_notificationFragment)
        }

        binding.filterImage.setOnClickListener{

            val bundle = Bundle()
            bundle.putInt(AppConsts.INTENT_ID, viewModel.intentId)
            findNavController().navigate(R.id.action_propertiesFragment_to_filterFragment, bundle)

        }

        binding.etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                when (viewModel.intentId) {
                    1 -> sharedViewModel.searchResidential(binding.etSearch.text.toString())
                    2 -> sharedViewModel.searchCommercial(binding.etSearch.text.toString())
                    3 -> sharedViewModel.searchAds(binding.etSearch.text.toString())
                }

                return@OnEditorActionListener true
            }
            false
        })

    }

}