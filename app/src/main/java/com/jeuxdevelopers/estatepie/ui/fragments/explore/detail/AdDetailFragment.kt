package com.jeuxdevelopers.estatepie.ui.fragments.explore.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentAdDetailBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.LeaseTermResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.explore.detail.adapters.ImagesAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AdDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdDetailBinding

    private val viewModel: AdDetailViewModel by viewModels()

    var dialogData = LeaseTermResponse.Data()

    private var id : String = ""

    private var jsonData : String = ""

    private var item : ExploreRecommendedResponse.Data.Recommended? = null

    private var shareUrl : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.leasingTerms()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBundleData()

        initCollectors()

        initClickListeners()

    }

    @SuppressLint("SetTextI18n")
    private fun setBundleData() {

        jsonData = arguments?.getString(AppConsts.DETAIL_ARGUMENTS,"").toString()

        if (jsonData!=null){
            item = Gson().fromJson(jsonData, ExploreRecommendedResponse.Data.Recommended::class.java)
            item?.let {

                if (it.property_images?.size!! > 0){

                    initImagesRecycler(it.property_images)
                    binding.noPopularDataFound.visibility = View.GONE

                }else{
                    binding.noPopularDataFound.visibility = View.VISIBLE
                }

                addChips(it.amenities)

//                viewModel.leasingTerms = it.lease

                binding.tvName.text = it.name
                binding.tvAddress.text = it.address
                binding.tvAmount.text = "$" + it.price.toString()
                binding.tvCategory.text = it.category_type
                binding.tvAvail.text = it.available.toString()

                if (it.listed_by.isNotEmpty())
                binding.tvListedBy.text = "Listed By: " + it.listed_by
                else
                    binding.tvListedBy.text = "Listed By: N/A"
                binding.tvMultiUnits.text = "Multi Units: " + it.multiunits.toString()
                binding.tvBroker.text = "Broker: " + it.broker
                if (it.beds.isNotEmpty())
                binding.tvBeds.text = "Bedrooms: " + it.beds
                else
                    binding.tvBeds.text = "Bedrooms: N/A"
                binding.tvFees.text = "Fees: " + it.fees
                binding.tvPropertyType.text = "Property Type: " + it.property_type
                binding.tvCreatedAt.text = "Added: " + it.created_on
                binding.tvDescription.text = "" + it.description
            }
        }

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.leasingTermsUiState){
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
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    dialogData = it.result?.data!!

                }
            }
        }
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnGetInTouch.setOnClickListener {

//            findNavController().navigate(R.id.action_adDetailFragment_to_companyProfileFragment)
            ToastUtility.errorToast(requireContext(), getString(R.string.favorite_before_login))

        }

        binding.btnLeasingTerms.setOnClickListener{

            showLeaseTermsDialog()
        }

        binding.fabHeart.setOnClickListener {
            ToastUtility.errorToast(requireContext(), getString(R.string.favorite_before_login))
        }

        binding.fabShare.setOnClickListener {

            shareProperty()

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

        if (dialogData.content.isNotEmpty())
        descTxt.text = dialogData.content
        else
            descTxt.text = "N/A"

        crossBtn.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun initImagesRecycler(propertyImages: List<ExploreRecommendedResponse.Data.Recommended.PropertyImage>?) {
        val adapter = ImagesAdapter()
        adapter.submitList(propertyImages)
        binding.recyclerImages.adapter = adapter
        binding.arIndicator.attachTo(binding.recyclerImages, true)
        val snapHelper = PagerSnapHelper()
        binding.recyclerImages.onFlingListener = null
        snapHelper.attachToRecyclerView(binding.recyclerImages)
    }


    @SuppressLint("InflateParams")
    private fun addChips(list: List<ExploreRecommendedResponse.Data.Recommended.Amenity>?) {
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


}