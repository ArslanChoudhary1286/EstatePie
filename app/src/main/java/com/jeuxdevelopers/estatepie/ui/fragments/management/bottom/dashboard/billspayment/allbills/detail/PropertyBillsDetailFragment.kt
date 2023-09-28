package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.billspayment.allbills.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentPropertyBillsDetailBinding
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.PropertyBillsResponse
import com.jeuxdevelopers.estatepie.utils.AppConsts

class PropertyBillsDetailFragment : Fragment() {

    private lateinit var binding: FragmentPropertyBillsDetailBinding

    private val viewModel: PropertyBillsDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertyBillsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initIntentData()

        initClickListeners()

    }

    private fun initIntentData() {

        val jsonData = arguments?.getString(AppConsts.BILL_MODEL)
        if (jsonData != null) {

                Gson().fromJson(jsonData, PropertyBillsResponse.Data.Bill::class.java)
                .let {

                binding.tvProperty.text = it!!.property_name
                binding.tvAmount.text = it.amount.toString()
                binding.tvDateTime.text = it.date
                binding.tvBillType.text = it.bill_type
                Glide.with(binding.root).load(it.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_house_gray)
                    .error(R.drawable.ic_house_gray)
                    .dontAnimate()
                    .into(binding.invoiceImage)

            }
        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}