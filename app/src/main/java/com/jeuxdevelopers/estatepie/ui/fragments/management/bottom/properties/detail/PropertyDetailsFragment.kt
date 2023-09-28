package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.jeuxdevelopers.estatepie.BuildConfig
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentPropertyDetailsBinding
import com.jeuxdevelopers.estatepie.network.responses.explore.LeaseTermResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertiesDetailsResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.detail.adapter.PropertyDetailImagesAdapter
import com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.detail.adapter.PropertyNotesAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PropertyDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPropertyDetailsBinding

    private val viewModel: PropertyDetailsViewModel by viewModels()

    private var dialogData = LeaseTermResponse.Data()

    private val adapter = PropertyDetailImagesAdapter()

    private var notesAdapter = PropertyNotesAdapter(::onCrossClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertyDetailsBinding.inflate(inflater, container, false)

        setBundleData()

        initAdapter()

        initCollectors()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    @SuppressLint("SetTextI18n")
    private fun setBundleData() {

        if (arguments?.getString(AppConsts.PROPERTY_ID) != null){

            viewModel.id = arguments?.getString(AppConsts.PROPERTY_ID).toString()
            viewModel.propertiesDetailsRequest.id = viewModel.id

            viewModel.getPropertiesDetails()

        }

    }

    private fun initAdapter() {

        binding.recyclerImages.adapter = adapter
        binding.arIndicator.attachTo(binding.recyclerImages, true)
        val snapHelper = PagerSnapHelper()
        binding.recyclerImages.onFlingListener = null
        snapHelper.attachToRecyclerView(binding.recyclerImages)

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

        collectLatestLifecycleFlow(viewModel.addNoteToTenantsUiState){
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
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), it.result?.message)
                    viewModel.getPropertiesDetails()

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.deleteNotesByIdUiState){
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
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), it.result?.message)
                    viewModel.getPropertiesDetails()

                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUi(data: PropertiesDetailsResponse.Data?) {

        data.let {

            adapter.submitList(it?.property_images)

            viewModel.shareUrl = it?.share_link.toString()

            viewModel.tenantId = it?.property_assign_id.toString()

            viewModel.id = it?.id.toString()

            binding.tvName.text = it?.name + "(${it?.unit.toString()})"
            binding.tvAddress.text = it?.address
            binding.tvCategory.text = "Category: " + it?.category_type
            binding.tvAvail.text = "Availability Status: " + it?.status.toString()
            binding.tvMultiUnits.text = "Multi Units: No"
            binding.tvCreatedAt.text = "Added: " + it?.created_on
            if (it?.description?.isNotEmpty() == true)
            binding.tvDescription.text = it.description
            else
                binding.tvDescription.text = "N/A"

            if (!it?.property_assign_email?.isEmpty()!!){

                binding.tvTenantName.text = "Name: " + it.property_assign_to
                binding.tvTenantPhone.text = "Phone: " + it.property_assign_phone
                binding.tvTenantEmail.text = "Email: " + it.property_assign_email

            }else{

                binding.cardTenantInformation.visibility = View.GONE
                binding.tvTenantInformation.visibility = View.GONE

            }


            notesAdapter.submitList(data?.notes)
            binding.notesRecycler.adapter = notesAdapter

        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnLeasingTerms.setOnClickListener{

            showLeaseTermsDialog()

        }

        binding.btnAddNotes.setOnClickListener { addNotes(binding.etNotes.text.toString()) }


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

        val content = dialogData.title
        if (content.isNotEmpty())
            descTxt.text = content
        else
            descTxt.text = "N/A"
//        descTxt.text = dialogData.content
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

    private fun addNotes(notes: String) {

        viewModel.addNoteToTenantsRequest.tenant_id = ""
        viewModel.addNoteToTenantsRequest.property_id = viewModel.id
        viewModel.addNoteToTenantsRequest.message = notes
        viewModel.addNoteToTenantsRequest.type = "property"
        viewModel.addNotes()
        binding.etNotes.text = null

    }

    private fun onCrossClicked(item : PropertiesDetailsResponse.Data.Note, position: Int){

        deleteNote(item.id, position)

    }

    private fun deleteNote(id: Int, position: Int) {

        viewModel.delNotesByIdRequest.id = id.toString()
        viewModel.deleteNotes()

    }

}