package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentTenantsAdsDetailBinding
import com.jeuxdevelopers.estatepie.models.chat.inbox.InboxModel
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.location.LocationPickerDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore.detail.adapter.TenantsImagesAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TenantsAdsDetailFragment : Fragment() {

    private lateinit var binding: FragmentTenantsAdsDetailBinding

    private val viewModel: TenantsAdsDetailViewModel by viewModels()

//    var dialogData = LeaseTermResponse.Data()

    val adapter = TenantsImagesAdapter()

    private var id : String = ""

    private var shareUrl : String = ""

    private val inboxRefrencec: CollectionReference =
        Firebase.firestore.collection(AppConsts.CHAT_COLLECTION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTenantsAdsDetailBinding.inflate(inflater, container, false)

        initCollectors()

        setBundleData()

        initImagesRecycler()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

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

                    setInboxModel(it.result?.data)

                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUi(data: AdsDetailResponse.Data?) {

        data.let {

            adapter.submitList(it?.property_images)

            addChips(it?.amenities)

            shareUrl = it?.share_link.toString()

            binding.tvName.text = it?.name
            binding.tvAddress.text = it?.address
            binding.tvAmount.text = it?.price.toString()
            binding.tvCategory.text = it?.category_type
            binding.tvAvail.text = it?.status.toString()

            binding.tvListedBy.text = "Listed By: " + it?.listed_by
            binding.tvMultiUnits.text = "Multi Units: " + it?.multiunits.toString()
            binding.tvBroker.text = "Broker: " + it?.broker
            binding.tvBeds.text = "Bedrooms: " + it?.beds
            binding.tvFees.text = "Fees: " + it?.fees
            binding.tvPropertyType.text = "Property Type: " + it?.property_type
            binding.tvCreatedAt.text = "Added: " + it?.created_on
            binding.tvDescription.text = "" + it?.description

            if (it?.is_favourite == 1){
                binding.fabHeart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.red))
            }

            viewModel.lasingTerms = it?.lease_terms.toString()

        }

    }

    @SuppressLint("SetTextI18n")
    private fun setBundleData() {

        if (arguments?.getString(AppConsts.PROPERTY_ID) != null){

            id = arguments?.getString(AppConsts.PROPERTY_ID).toString()
            viewModel.propertiesDetailsRequest.id = id
            viewModel.getPropertiesDetails()

        }

    }

    private fun setInboxModel(data: AdsDetailResponse.Data?) {

        if (data != null){

            data.let {

                viewModel.inboxModel.created_at = System.currentTimeMillis()

                viewModel.inboxModel.landlord_id = data.listed_id
                viewModel.inboxModel.landlord_name = data.listed_by
                viewModel.inboxModel.landlord_image = data.listed_image

                viewModel.inboxModel.tenant_id = viewModel.getCurrentUser()!!.id
                viewModel.inboxModel.tenant_name = viewModel.getCurrentUser()!!.name
                viewModel.inboxModel.tenant_image = viewModel.getCurrentUser()!!.details.image

                viewModel.inboxModel.property_id = data.id
                viewModel.inboxModel.property_name = data.name
                viewModel.inboxModel.property_image = data.property_images[0].image

            }


        }


    }

    fun getInbox() {

        val waitingDialog = WaitingDialog(requireContext())
        waitingDialog.show()

        inboxRefrencec
//            .orderBy(AppConsts.MESSAGE_TIME, Query.Direction.DESCENDING)
            .whereEqualTo(AppConsts.TENANT_ID, viewModel.getCurrentUser()!!.id)
            .whereEqualTo(AppConsts.LANDLORD_ID, viewModel.inboxModel.landlord_id)
            .get().addOnCompleteListener{

                var docsId: String = ""

                if (it.result.isEmpty){

                    docsId = inboxRefrencec.document().id

                    inboxRefrencec.document(docsId).set(viewModel.inboxModel).addOnSuccessListener {
                        waitingDialog.dismiss()
                        val bundle = Bundle()
                        bundle.putString(AppConsts.DOCS_ID, docsId)
                        bundle.putString(AppConsts.USER_NAME, viewModel.inboxModel.landlord_name)
                        bundle.putString(AppConsts.USER_IMAGE, viewModel.inboxModel.landlord_name)
                        bundle.putString(AppConsts.PROPERTY_NAME, viewModel.inboxModel.property_name)
                        findNavController().navigate(R.id.action_tenantsAdsDetailFragment_to_chatFragment2, bundle)
                    }.addOnFailureListener{
                        waitingDialog.dismiss()
                        ToastUtility.errorToast(requireContext(), "unable to create chat.")
                    }

                }else{
                    docsId = it.result.documents[0].id
                    waitingDialog.dismiss()
                    val bundle = Bundle()
                    bundle.putString(AppConsts.DOCS_ID, docsId)
                    bundle.putString(AppConsts.USER_NAME, viewModel.inboxModel.landlord_name)
                    bundle.putString(AppConsts.USER_IMAGE, viewModel.inboxModel.landlord_name)
                    bundle.putString(AppConsts.PROPERTY_NAME, viewModel.inboxModel.property_name)
                    findNavController().navigate(R.id.action_tenantsAdsDetailFragment_to_chatFragment2, bundle)
                }
            }.addOnFailureListener{
                ToastUtility.errorToast(requireContext(), "unable to create chat.")
                waitingDialog.dismiss()
            }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnGetInTouch.setOnClickListener { getInbox() }

        binding.btnLeasingTerms.setOnClickListener{ showLeaseTermsDialog() }

        binding.fabHeart.setOnClickListener {

            viewModel.markToFavoriteRequest.id = id
            viewModel.markToFavorite()

        }

        binding.fabShare.setOnClickListener { shareProperty() }

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
        titleTxt.text = viewModel.lasingTitle
        descTxt.text = viewModel.lasingTerms
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
//            "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        shareUrl
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)

    }

    private fun initImagesRecycler() {

        binding.recyclerImages.adapter = adapter
        binding.arIndicator.attachTo(binding.recyclerImages, true)
        val snapHelper = PagerSnapHelper()
        binding.recyclerImages.onFlingListener = null
        snapHelper.attachToRecyclerView(binding.recyclerImages)

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