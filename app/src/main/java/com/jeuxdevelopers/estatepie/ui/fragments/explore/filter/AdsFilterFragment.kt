package com.jeuxdevelopers.estatepie.ui.fragments.explore.filter

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.RangeSlider
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentAdsFilterBinding
import com.jeuxdevelopers.estatepie.models.filter.FilterModel
import com.jeuxdevelopers.estatepie.ui.dialogs.adapter.DialogAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class AdsFilterFragment : Fragment() {

    private lateinit var binding: FragmentAdsFilterBinding

//    private val viewModel: AdsFilterViewModel by viewModels()

    private var dialogBinding: View? = null

    private var dialog: Dialog? = null

    var dialogAdapter = DialogAdapter(::onDialogItemClick)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdsFilterBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()

        initClickListeners()

        initRangeSlider()

        setAdapters()

    }

    private fun initDialog(){

        dialog = Dialog(requireContext())
        dialogBinding = layoutInflater.inflate(R.layout.dialog_input_properties, null)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setContentView(dialogBinding!!)
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(false)
        val crossBtn = dialogBinding?.findViewById<ImageView>(R.id.dialog_cross)
        dialogAdapter?.setHasStableIds(true)
        crossBtn?.setOnClickListener { dialog?.dismiss() }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{

            findNavController().navigateUp()

        }

        binding.btnApply.setOnClickListener {

            val navController = findNavController()
            navController.previousBackStackEntry?.savedStateHandle?.set(AppConsts.FILTER_DATA, pickDataForFilter())
            navController.popBackStack()

        }

        binding.tvUnit.setOnClickListener{

            showPropertyDialog(
                resources.getStringArray(R.array.unit_array).toMutableList(),
                getString(R.string.unit)
            )

        }

    }

    private fun showPropertyDialog(adapterList: MutableList<String>?, title: String) {

        val recyclerView = dialogBinding?.findViewById<RecyclerView>(R.id.dialog_recycler)
        val titleTextView = dialogBinding?.findViewById<TextView>(R.id.dialog_title)
        dialogAdapter.submitList(adapterList)
        recyclerView?.adapter = dialogAdapter
        titleTextView?.text = title
        dialog?.show()

    }

    private fun initRangeSlider() {

        binding.sliderRange.addOnSliderTouchListener(object :RangeSlider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: RangeSlider) {

            }

            @SuppressLint("SetTextI18n")
            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = slider.values
                binding.tvInitRange.text = (values[0].roundToInt().toString())
                binding.tvFinalRange.text = (values[1].roundToInt().toString())
            }

        })

    }

    private fun pickDataForFilter() : String{

        val filterModel = FilterModel()

        filterModel.keyword = binding.etEnterKeyword.text?.trim().toString()
        filterModel.location = binding.etSearchByLocation.text?.trim().toString()
        filterModel.sortByPrice = binding.etSortByPrice.text?.trim().toString()
        filterModel.category = binding.etSelectCategory.text?.trim().toString()
        filterModel.propertyType = binding.etPropertyType.text?.trim().toString()
        filterModel.bathRooms = binding.etSelectBathrooom.text?.trim().toString()
        filterModel.bedRooms = binding.etSelectBedrooom.text?.trim().toString()
        filterModel.forSaleRent = binding.etForSale.text?.trim().toString()
        filterModel.fees = binding.etFees.text?.trim().toString()
        filterModel.unit = binding.tvUnit.text?.trim().toString()
        filterModel.size = binding.etSize.text?.trim().toString()
        filterModel.smoking = binding.etSmoking.text?.trim().toString()
        filterModel.pets = binding.etPets.text?.trim().toString()
        filterModel.priceStartRange = binding.tvInitRange.text?.trim().toString().toInt()
        filterModel.priceEndRange = binding.tvFinalRange.text?.trim().toString().toInt()

        return Gson().toJson(filterModel)

    }

    private fun setAdapters() {

        // all item adapter
        val sortByPriceAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,R.id.adapterTxtView, resources.getStringArray(R.array.sort_by_price)
        )
        binding.etSortByPrice.setAdapter(sortByPriceAdapter)

        val categoryAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,R.id.adapterTxtView, resources.getStringArray(R.array.category_filter)
        )
        binding.etSelectCategory.setAdapter(categoryAdapter)

        val propertyTypeAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,R.id.adapterTxtView, resources.getStringArray(R.array.property_type)
        )
        binding.etPropertyType.setAdapter(propertyTypeAdapter)

        val forSaleRentAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,R.id.adapterTxtView, resources.getStringArray(R.array.for_sale_for_rent_filter)
        )

        val yesNoAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,R.id.adapterTxtView, resources.getStringArray(R.array.fees_filter)
        )

        binding.etForSale.setAdapter(forSaleRentAdapter)
        binding.etFees.setAdapter(yesNoAdapter)
        binding.etSmoking.setAdapter(yesNoAdapter)
        binding.etPets.setAdapter(yesNoAdapter)

    }

    private fun onDialogItemClick(data: String, position: Int) {
        binding.tvUnit.text = data
        dialog?.dismiss()
    }

}