package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.detail

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
import com.jeuxdevelopers.estatepie.databinding.FragmentBillDetailBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.network.responses.management.billing.UnPaidBillByTenantIdResponse
import com.jeuxdevelopers.estatepie.utils.AppConsts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillDetailFragment : Fragment() {

    private lateinit var binding: FragmentBillDetailBinding

    private val viewModel: BillDetailViewModel by viewModels()

    private var item : UnPaidBillByTenantIdResponse.Data.Bill? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillDetailBinding.inflate(inflater, container, false)
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
            item =
                Gson().fromJson(jsonData, UnPaidBillByTenantIdResponse.Data.Bill::class.java)
            item?.let {

                binding.tvProperty.text = item!!.property_name
                binding.tvAmount.text = item!!.amount.toString()
                binding.tvDateTime.text = item!!.date
                binding.tvBillType.text = item!!.bill_type
                Glide.with(binding.root).load(item!!.image)
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