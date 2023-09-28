package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentManagementAdDetailBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.LeaseTermResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore.detail.adapter.TenantsImagesAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ManagementAdDetailFragment : Fragment() {

    private lateinit var binding: FragmentManagementAdDetailBinding

    private val viewModel: ManagementAdDetailViewModel by viewModels()

    private var dialogData = LeaseTermResponse.Data()

    private val adapter = TenantsImagesAdapter()

    private var id : String = ""

    private var shareUrl : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagementAdDetailBinding.inflate(inflater, container, false)

        setBundleData()

        initAdapter()

        initCollectors()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initAdapter() {

        binding.recyclerImages.adapter = adapter
        binding.arIndicator.attachTo(binding.recyclerImages, true)
        val snapHelper = PagerSnapHelper()
        binding.recyclerImages.onFlingListener = null
        snapHelper.attachToRecyclerView(binding.recyclerImages)

    }

    @SuppressLint("SetTextI18n")
    private fun setBundleData() {

        if (arguments?.getString(AppConsts.PROPERTY_ID) != null){

            id = arguments?.getString(AppConsts.PROPERTY_ID).toString()
            viewModel.propertiesDetailsRequest.id = id
            viewModel.getPropertiesDetails()

        }

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
//        collectLatestLifecycleFlow(viewModel.leasingTermsUiState){
//            when(it){
//                is NetworkResult.Error -> {
//                    waitingDialog.dismiss()
//                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
//                    ToastUtility.errorToast(requireContext(), "Error : ${it.message}, ${it.result.toString()}")
//
//                }
//                is NetworkResult.Idle -> {
//                    //nothing to do this is just idle state
//                }
//                is NetworkResult.Loading -> {
//                    waitingDialog.show(status = "Loading...")
//                    Timber.d("Loading...")
//                }
//                is NetworkResult.Success -> {
//                    waitingDialog.dismiss()
//                    Timber.d("Success -> ${it.message}")
//                    Timber.d("Success Data -> ${it.result?.data.toString()}")
//
//                    dialogData = it.result?.data!!
//
//                }
//            }
//        }

        collectLatestLifecycleFlow(viewModel.markToFavoriteUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), "Error : ${it.message}, ${it.result.toString()}")

                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                    Timber.d("Loading...")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()

                    ToastUtility.successToast(requireContext(), it.result?.message)

                    if (it.result?.message?.contains("Remove") == true){
                        binding.fabHeart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white))

                    }else{
                        binding.fabHeart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.red))

                    }

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.propertiesDetailsUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), "Error : ${it.message}, ${it.result.toString()}")

                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                    Timber.d("Loading...")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()

                    setUi(it.result?.data)

                }
            }
        }
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnGetInTouch.setOnClickListener {

//            findNavController().navigate(R.id.action_managementAdDetailFragment_to_managementCompnyProfileFragment)
            ToastUtility.successToast(requireContext(), "Chat not implement yet")

        }

        binding.btnLeasingTerms.setOnClickListener{

            showLeaseTermsDialog()

        }

        binding.fabHeart.setOnClickListener {

            viewModel.markToFavoriteRequest.id = id
            viewModel.markToFavorite()

        }

        binding.fabShare.setOnClickListener {
            shareProperty()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUi(data: AdsDetailResponse.Data?) {

        data.let {

            adapter.submitList(it?.property_images)

            addChips(it?.amenities)

            shareUrl = it?.share_link.toString()
            viewModel.leaseTerms = it?.lease_terms.toString()

            binding.tvName.text = it?.name
            binding.tvAddress.text = it?.address
            binding.tvAmount.text = "$" + it?.price.toString()
            binding.tvCategory.text = it?.category_type
            binding.tvAvail.text = it?.status.toString()

            "Listed By: ".setTextOrHide(binding.tvListedBy, it?.listed_by)
            if (it?.multiunits == 0) "Multi Units: ".setTextOrHide(binding.tvMultiUnits,  "No")
            else "Multi Units: ".setTextOrHide(binding.tvMultiUnits,  "Yes")
            "Broker: ".setTextOrHide(binding.tvBroker, it?.broker)
            "Area: ".setTextOrHide(binding.tvSize, it?.size.toString() + " " + it?.unit)
            "Bedrooms: ".setTextOrHide(binding.tvBeds, it?.beds)
            "Fees: ".setTextOrHide(binding.tvFees, it?.fees)
            "Pets: ".setTextOrHide(binding.tvPets, it?.pets)
            "Smoking: ".setTextOrHide(binding.tvSmoking, it?.smoking)
            "Bathrooms: ".setTextOrHide(binding.tvBathrooms, it?.bathroom)
            "Property Type: ".setTextOrHide(binding.tvPropertyType, it?.property_type)
            "Added: ".setTextOrHide(binding.tvCreatedAt, it?.created_on)
            "".setTextOrHide(binding.tvDescription, it?.description)

            if (it?.is_favourite == 1){
                binding.fabHeart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.red))
            }

        }

    }

    @SuppressLint("SetTextI18n")
    private fun String.setTextOrHide(textView: TextView, text: String?) {
        textView.apply {
            if (text.isNullOrEmpty()) setText(this@setTextOrHide + "N/A")
            else setText(this@setTextOrHide + text)
        }
    }

    private fun showLeaseTermsDialog() {

        val dialogBinding = layoutInflater.inflate(R.layout.dialog_leasing_terms, null)
        val dialog = Dialog(binding.btnLeasingTerms.context)
        dialog.setContentView(dialogBinding)
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val crossBtn = dialogBinding.findViewById<ImageView>(R.id.dialog_cross)
        val titleTxt = dialogBinding.findViewById<TextView>(R.id.dialog_title)
        val descTxt = dialogBinding.findViewById<TextView>(R.id.dialog_description)
        titleTxt.text = getString(R.string.leasing_terms_txt)
        descTxt.text = viewModel.leaseTerms
        crossBtn.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun shareProperty() {

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)

    }

    @SuppressLint("InflateParams")
    private fun addChips(list: List<AdsDetailResponse.Data.Amenity>?) {
        if (list != null) {
            for (amenities in list) {
                val chipView = Chip(requireContext())
                chipView.text = amenities.name
                chipView.chipIcon

                Glide.with(this)
                    .load(amenities.icon)
                    .into(object : CustomTarget<Drawable>(200, 200) { //size might be omitted

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            chipView.chipIcon = errorDrawable
                        }

                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            chipView.chipIcon = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            chipView.chipIcon = placeholder
                        }

                    })

                chipView.chipIconTint =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue))
                chipView.chipStrokeWidth = 0.0f
                chipView.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
                chipView.shapeAppearanceModel =
                    ShapeAppearanceModel().withCornerSize(com.intuit.sdp.R.dimen._6sdp.toFloat())
                chipView.isClickable = false
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()


                binding.chipGroup.addView(chipView)
            }
        }
    }

}