package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TenantsPaymentFragment : Fragment() {


    private lateinit var binding: FragmentTenantsPaymentBinding
    private lateinit var viewModel: TenantsPaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.tvPaymentMake.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsPaymentFragment_to_makePaymentFragment)
        }

        binding.tvPaymentHistory.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsPaymentFragment_to_paymentHistoryFragment)
        }

        binding.tvPaymentSchedule.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsPaymentFragment_to_paymentScheduleFragment)
        }

        binding.tvPaymentSetting.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsPaymentFragment_to_paymentSettingsFragment)
        }
    }

}