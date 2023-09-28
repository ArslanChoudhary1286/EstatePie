package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantProfileBinding
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import dagger.hilt.android.AndroidEntryPoint
import io.opencensus.internal.StringUtils

@AndroidEntryPoint
class TenantProfileFragment : Fragment() {

    private lateinit var binding: FragmentTenantProfileBinding

    private  val viewModel: TenantProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initClickListeners()

    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {

        viewModel.getCurrentUser()?.apply {
            binding.tvUserName.text = details.first_name +" "+ details.last_name
            binding.textView8.text = email
            Glide.with(requireContext()).load(details.image_url)                .centerCrop()
                .placeholder(R.drawable.ic_house_gray)
                .error(R.drawable.ic_house_gray)
                .dontAnimate()
                .into(binding.imgProfile)
            binding.tvPhoneNo.text = details.phone
            binding.tvAddress.text = details.address

        }

    }

    private fun initClickListeners() {
        binding.etEdit.setOnClickListener{
            findNavController().navigate(R.id.action_tenantsProfileFragment_to_tenantsEditProfileFragment)
        }
        binding.tvVehicleInformation.setOnClickListener {
            findNavController().navigate(R.id.action_tenantsProfileFragment_to_tenantsVehicleFragment)
        }

        binding.navImage.setOnClickListener {
            (requireActivity() as ManagementDrawerListener).openDrawer()
        }
    }

}