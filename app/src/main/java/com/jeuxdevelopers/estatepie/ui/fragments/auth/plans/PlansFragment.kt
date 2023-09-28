package com.jeuxdevelopers.estatepie.ui.fragments.auth.plans

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentPlansBinding
import com.jeuxdevelopers.estatepie.network.responses.plans.SubscriptionPlansResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.activities.managment.MainManagementActivity
import com.jeuxdevelopers.estatepie.ui.activities.managment.MainManagementViewModel
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PlansFragment : Fragment() {

    private lateinit var binding: FragmentPlansBinding

    private val viewModel: PlansViewModel by viewModels()

    private val mainViewModel: MainManagementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlansBinding.inflate(inflater, container, false)

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getSubscriptionPlans()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.subscriptionPlansUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    SetFreePlan(it.result?.data?.free)
                    SetStandradPlan(it.result?.data?.standard)
                    SetPremiumPlan(it.result?.data?.premium)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.makeSubscriptionUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), it.result?.message)
                    findNavController().navigateUp()

                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun SetPremiumPlan(premium: SubscriptionPlansResponse.Data.Premium?) {

        premium?.data.let {

//            binding.premiumTitle.text = it?.name
            binding.premiumSubTitle.text = it?.subtitle

            binding.premiumDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(it?.description, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(it?.description)
            }

        }

        premium?.monthly.let {

            viewModel.pm = it?.plan_id.toString()

            binding.premiumMonthlyText.text = it?.name
            binding.premiumMonthlyPrice.text = it?.price + " " + premium?.data?.currencyIsoCode

        }

        premium?.yearly.let {

            viewModel.py = it?.plan_id.toString()

            binding.premiumYearlyText.text = it?.name
            binding.premiumYearlyPrice.text = it?.price + " " + premium?.data?.currencyIsoCode

        }

    }

    @SuppressLint("SetTextI18n")
    private fun SetStandradPlan(standard: SubscriptionPlansResponse.Data.Standard?) {

        standard?.data.let {

//            binding.standardTitle.text = it?.name
            binding.standardSubTitle.text = it?.subtitle

            binding.standardDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(it?.description, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(it?.description)
            }

        }

        standard?.monthly.let {

            viewModel.sm = it?.plan_id.toString()

            binding.standardMonthlyText.text = it?.name
            binding.standardMonthlyPrice.text = it?.price + " " + standard?.data?.currencyIsoCode

        }

        standard?.yearly.let {

            viewModel.sy = it?.plan_id.toString()

            binding.standardYearlyText.text = it?.name
            binding.standardYearlyPrice.text = it?.price + " " + standard?.data?.currencyIsoCode

        }

    }

    @SuppressLint("SetTextI18n")
    private fun SetFreePlan(free: SubscriptionPlansResponse.Data.Free?) {


        free?.let {

//            binding.freeTitle.text = it?.name
            binding.freeSubTitle.text = it.subtitle

            binding.freeDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(it.description, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(it.description)
            }

        }

        free?.monthly.let {

            viewModel.fm = it?.plan_id.toString()

            binding.freeMonthlyText.text = it?.name
            binding.freeMonthlyPrice.text = it?.price + " " + free?.currencyIsoCode

        }

        free?.yearly.let {

            viewModel.fy = it?.plan_id.toString()

            binding.freeYearlyText.text = it?.name
            binding.freeYearlyPrice.text = it?.price + " " + free?.currencyIsoCode

        }



    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.premiumBtn.isEnabled = false
        binding.standardBtn.isEnabled = false
        binding.freeBtn.isEnabled = false

        binding.freeBtn.setOnClickListener {

            if (viewModel.checkId == 5){

                viewModel.request.plan_id = viewModel.fm
                viewModel.request.token = viewModel.getCurrentUser()?.access_token.toString()
                viewModel.makeSubscriptions()

            }else if (viewModel.checkId == 6){

                viewModel.request.plan_id = viewModel.fy
                viewModel.request.token = viewModel.getCurrentUser()?.access_token.toString()
                viewModel.makeSubscriptions()

            }

        }

        binding.standardBtn.setOnClickListener {
            if (viewModel.checkId == 3){

//                mainViewModel.purchaseWithSubscription(viewModel.planName.lowercase(), requireActivity())

                viewModel.request.plan_id = viewModel.sm
                viewModel.request.token = viewModel.getCurrentUser()?.access_token.toString()
                viewModel.makeSubscriptions()

            }else if (viewModel.checkId == 4){

                viewModel.request.plan_id = viewModel.sy
                viewModel.request.token = viewModel.getCurrentUser()?.access_token.toString()
                viewModel.makeSubscriptions()

            }
        }

        binding.premiumBtn.setOnClickListener {

            if (viewModel.checkId == 1){

                viewModel.request.plan_id = viewModel.pm
                viewModel.request.token = viewModel.getCurrentUser()?.access_token.toString()
                viewModel.makeSubscriptions()

            }else if (viewModel.checkId == 2){

                viewModel.request.plan_id = viewModel.py
                viewModel.request.token = viewModel.getCurrentUser()?.access_token.toString()
                viewModel.makeSubscriptions()

            }
        }

        binding.premiumBtnMonthly.setOnClickListener { updateView(1) }
        binding.premiumBtnYearly.setOnClickListener {  updateView(2) }
        binding.standardBtnMonthly.setOnClickListener {  updateView(3) }
        binding.standardBtnYearly.setOnClickListener {  updateView(4) }
        binding.freeBtnMonthly.setOnClickListener {  updateView(5) }
        binding.freeBtnYearly.setOnClickListener {  updateView(6) }

        binding.btnSkip.setOnClickListener {
            Intent(
                requireContext(),
                MainManagementActivity::class.java
            ).apply {
                startActivity(this)
            }
        }

    }

    private fun updateView(i: Int) {

        viewModel.checkId = i

        when(i){
            1 -> {

                binding.premiumBtn.isEnabled = true
                binding.standardBtn.isEnabled = false
                binding.freeBtn.isEnabled = false

                binding.premiumBtnMonthly.setBackgroundColor(resources.getColor(R.color.blue))
                binding.premiumBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
                binding.standardBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.standardBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
                binding.freeBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.freeBtnYearly.setBackgroundColor(resources.getColor(R.color.white))

                viewModel.planName = "Premium_Monthly"

            } 2 ->{

            binding.premiumBtn.isEnabled = true
            binding.standardBtn.isEnabled = false
            binding.freeBtn.isEnabled = false

            binding.premiumBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
            binding.premiumBtnYearly.setBackgroundColor(resources.getColor(R.color.blue))
            binding.standardBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
            binding.standardBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
            binding.freeBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
            binding.freeBtnYearly.setBackgroundColor(resources.getColor(R.color.white))

            viewModel.planName = "Premium_Yearly"

            } 3 ->{

            binding.premiumBtn.isEnabled = false
            binding.standardBtn.isEnabled = true
            binding.freeBtn.isEnabled = false

                binding.premiumBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.premiumBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
                binding.standardBtnMonthly.setBackgroundColor(resources.getColor(R.color.blue))
                binding.standardBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
                binding.freeBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.freeBtnYearly.setBackgroundColor(resources.getColor(R.color.white))

            viewModel.planName = "Standard_Monthly"

            } 4 ->{

            binding.premiumBtn.isEnabled = false
            binding.standardBtn.isEnabled = true
            binding.freeBtn.isEnabled = false

                binding.premiumBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.premiumBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
                binding.standardBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.standardBtnYearly.setBackgroundColor(resources.getColor(R.color.blue))
                binding.freeBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.freeBtnYearly.setBackgroundColor(resources.getColor(R.color.white))

            viewModel.planName = "Standard_Yearly"

            } 5 ->{

            binding.premiumBtn.isEnabled = false
            binding.standardBtn.isEnabled = false
            binding.freeBtn.isEnabled = true

                binding.premiumBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.premiumBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
                binding.standardBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.standardBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
                binding.freeBtnMonthly.setBackgroundColor(resources.getColor(R.color.blue))
                binding.freeBtnYearly.setBackgroundColor(resources.getColor(R.color.white))

            viewModel.planName = "Free_Monthly"

            } 6 ->{

            binding.premiumBtn.isEnabled = false
            binding.standardBtn.isEnabled = false
            binding.freeBtn.isEnabled = true

                binding.premiumBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.premiumBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
                binding.standardBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.standardBtnYearly.setBackgroundColor(resources.getColor(R.color.white))
                binding.freeBtnMonthly.setBackgroundColor(resources.getColor(R.color.white))
                binding.freeBtnYearly.setBackgroundColor(resources.getColor(R.color.blue))

            viewModel.planName = "Free_Yearly"

            }

        }
    }

    private fun onItemClicked(item : SubscriptionPlansResponse.Data){

        val bundle = Bundle()
//        bundle.putString(AppConsts.INTENT_ID, item.id.toString())
        findNavController().navigate(R.id.action_requestsFragment_to_maintenanceRequestDetailFragment, bundle)

    }

}