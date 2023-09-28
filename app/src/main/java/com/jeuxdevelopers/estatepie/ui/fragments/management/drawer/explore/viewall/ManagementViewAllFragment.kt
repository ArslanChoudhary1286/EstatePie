package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.viewall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentManagementViewAllBinding
import com.jeuxdevelopers.estatepie.models.filter.FilterModel
import com.jeuxdevelopers.estatepie.network.responses.explore.ViewAllRecomResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.fragments.explore.viewall.adapters.ViewAllRecomAdapter
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ManagementViewAllFragment : Fragment() {

    private lateinit var binding: FragmentManagementViewAllBinding

    private val viewModel: ManagementViewAllViewModel by viewModels()

    var adapter = ViewAllRecomAdapter(::onRecommendedItemClicked)

    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagementViewAllBinding.inflate(inflater, container, false)

        initNavController()

        initRecommendedRecycler()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getViewAllRecommendedList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCollectors()

        initClickListeners()

        getDataToApplyFilter()

    }

    private fun initNavController() {

        navController = findNavController()

    }

    private fun initRecommendedRecycler() {

        binding.recyclerAdsRecommended.adapter = adapter

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.viewAllUiState){
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
                    Timber.d("Signing up")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
//                    Timber.d("Success Data -> ${it.result?.data.toString()}")
//                    it.result?.data.

                    if (viewModel.recommendList.isEmpty())
                        viewModel.recommendList.addAll(it.result?.data?.recommended!!)

                    adapter.submitList(viewModel.recommendList)


                }
            }
        }
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{

            findNavController().navigateUp()

        }

        binding.filterImage.setOnClickListener{

            navController!!.navigate(R.id.action_managementViewAllFragment_to_managementFilterFragment)

        }

    }

    private fun onRecommendedItemClicked(item : ViewAllRecomResponse.Data.Recommended){

        val bundle = Bundle()
        bundle.putString(AppConsts.PROPERTY_ID, item.id.toString())
        findNavController().navigate(R.id.action_managementViewAllFragment_to_managementAdDetailFragment, bundle)

    }

    private fun getDataToApplyFilter() {

        navController!!.currentBackStackEntry?.savedStateHandle?.getLiveData<String>(AppConsts.FILTER_DATA)?.observe(
            viewLifecycleOwner) { result ->
            // Do something with the result.

            val filterData: FilterModel =  Gson().fromJson(result, FilterModel::class.java)

            var filteredList = viewModel.recommendList

            if (filterData.keyword.isNotEmpty())
                filteredList = filteredList.filterByKeyword(filterData.keyword.lowercase()).toMutableList()

            if (filterData.location.isNotEmpty())
                filteredList = filteredList.filterByLocation(filterData.location.lowercase()).toMutableList()

            if (filterData.sortByPrice.equals(resources.getString(R.string.low_to_high)))
                filteredList = filteredList.sortByPriceLowToHigh().toMutableList()
            else if (filterData.sortByPrice.equals(resources.getString(R.string.high_to_low)))
                filteredList = filteredList.sortByPriceHighToLow().toMutableList()

            if (filterData.category.isNotEmpty())
                filteredList = filteredList.filterByCategory(filterData.category).toMutableList()

            if (filterData.propertyType.isNotEmpty())
                filteredList = filteredList.filterByPropertyType(filterData.propertyType).toMutableList()

            if (filterData.bathRooms.isNotEmpty())
                filteredList = filteredList.filterByBedRooms(filterData.bathRooms).toMutableList()

            if (filterData.bedRooms.isNotEmpty())
                filteredList = filteredList.filterByBedRooms(filterData.bedRooms).toMutableList()

            if (filterData.forSaleRent.isNotEmpty())
                filteredList = filteredList.filterByForSaleRent(filterData.forSaleRent).toMutableList()

            if (filterData.fees.isNotEmpty())
                filteredList = filteredList.filterByFees(filterData.fees).toMutableList()

            if (filterData.unit.isNotEmpty())
                filteredList = filteredList.filterByUnit(filterData.unit).toMutableList()

            if (filterData.size.isNotEmpty())
                filteredList = filteredList.filterBySize(filterData.size).toMutableList()

            if (filterData.smoking.isNotEmpty())
                filteredList = filteredList.filterBySmoking(filterData.smoking).toMutableList()

            if (filterData.pets.isNotEmpty())
                filteredList = filteredList.filterByPets(filterData.pets).toMutableList()

            if ((!filterData.priceStartRange.equals(resources.getString(R.string.price_one)))
                || (!filterData.priceEndRange.equals(resources.getString(R.string.price_5k))))
                filteredList = filteredList.filterByPriceRange(
                    filterData.priceStartRange,
                    filterData.priceEndRange).toMutableList()

            adapter.submitList(filteredList)

        }


    }

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByKeyword(keyword: String) =
        this.filter { it.name.lowercase().contains(keyword)}

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByLocation(location: String) =
        this.filter { it.address.lowercase().contains(location) }

    private fun List<ViewAllRecomResponse.Data.Recommended>.sortByPriceLowToHigh() =
        this.sortedBy { it.price }

    private fun List<ViewAllRecomResponse.Data.Recommended>.sortByPriceHighToLow() =
        this.sortedByDescending { it.price }

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByCategory(categoryType: String) =
        this.filter { it.category_type == categoryType }

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByPropertyType(propertyType: String) =
        this.filter { it.property_type == propertyType }

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByBedRooms(bedRooms: String) =
        this.filter { it.beds == bedRooms }

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByForSaleRent(forSaleRent: String) =
        this.filter { it.purpose == forSaleRent }

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByFees(fees: String) =
        this.filter { it.fees == fees }

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByUnit(unit: String) =
        this.filter { it.unit == unit }

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterBySize(size: String) =
        this.filter { it.size.toString() == size }

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterBySmoking(smoking: String) =
        this.filter { it.fees == smoking }
    // smoking field not found so therefore filter with fee

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByPets(pets: String) =
        this.filter { it.fees == pets }
    // smoking field not found so therefore filter with fee

    private fun List<ViewAllRecomResponse.Data.Recommended>.filterByPriceRange(startRange: Int, endRange: Int) =
        this.filter { (it.price >= startRange) && (it.price <= endRange) }

}