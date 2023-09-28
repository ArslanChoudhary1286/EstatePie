package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.rentfees


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentRentFeesBinding
import com.jeuxdevelopers.estatepie.enums.Plans
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.RentFeesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RentFeesFragment : Fragment() {

    private lateinit var binding: FragmentRentFeesBinding

    private val viewModel: RentFeesViewModel by viewModels()

    lateinit var barChart: BarChart

    lateinit var barData: BarData

    lateinit var barDataSet: BarDataSet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRentFeesBinding.inflate(inflater,container,false)

        if (viewModel.userPlan() == Plans.STANDARD
            || viewModel.userPlan() == Plans.PREMIUM){

            binding.paidView.visibility = View.VISIBLE
            binding.freeView.visibility = View.GONE

        }else{

            binding.paidView.visibility = View.GONE
            binding.freeView.visibility = View.VISIBLE

        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.userPlan() == Plans.STANDARD
            || viewModel.userPlan() == Plans.PREMIUM){

            viewModel.getRentFees()

            viewModel.getMultiListingProperty()

        }

    }

    override fun onResume() {
        super.onResume()

        if (viewModel.userPlan() == Plans.STANDARD
            || viewModel.userPlan() == Plans.PREMIUM){

            initCollectors()

            setAdapters()

        }

    }

    private fun setAdapters() {

        // all item adapter
        val allAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_string_adapter, R.id.adapterTxtView, resources.getStringArray(R.array.lease_duration_array)
        )
        binding.etMonths.setAdapter(allAdapter)

        binding.etMonths.setOnItemClickListener{parent, view, position, id ->

            val month = binding.etMonths.text.toString()

            if (position >= 9){
                viewModel.request.months = month.substring(0, 2)
            }else{
                viewModel.request.months = month.substring(0, 1)
            }
            viewModel.getRentFees()

        }

    }

    @SuppressLint("SetTextI18n")
    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.rentFeeUiState){
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
                    waitingDialog.show(status = "Loading ...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    binding.totalCollected.text  = it.result?.data?.total_collected

                    binding.vacant.text  = it.result?.data?.vacant_property.toString()

                    setPropertiesChart(it.result?.data?.chart)

                    setBillingChart(it.result?.data)

                    setMaintenanceChart(it.result?.data?.request_data?.maintenance)

                    setIncidentChart(it.result?.data?.request_data?.incident)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.propertyMultiListingUiState){
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
                    waitingDialog.show(status = "Loading ...")
                    Timber.d("explore ads")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    setPropertyNameAdapter(it.result?.data)

                }
            }
        }

    }

    private fun setPropertyNameAdapter(properties: List<MultiListingPropertyResponse.Data>?) {

        val array = Array<String>(properties!!.size){""}

        properties.forEachIndexed { index, element ->
            array[index] = element.name
        }

        val propertyAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,
            R.id.adapterTxtView, array)

        binding.etAllProperties.setAdapter(propertyAdapter)

        binding.etAllProperties.setOnItemClickListener{parent, view, position, id ->

//            val keyword = binding.etAllProperties.text.toString()
//            viewModel.request.keyword = "keyword"

            viewModel.getRentFees()

        }

    }


    private fun setPropertiesChart(chart: List<RentFeesResponse.Data.Chart>?) {

        val barEntriesList =  ArrayList<BarEntry>()
        val titleList = ArrayList<String>()

        chart!!.forEachIndexed{index, data ->
            barEntriesList.add(BarEntry(index.toFloat(), data.value.toFloat()))
            titleList.add(data.key)
        }

        barChart = binding.idPropertyChart

        barChart.axisRight.isEnabled = false

        barChart.setDrawBarShadow(false)

        barChart.setDrawValueAboveBar(true)

        barDataSet = BarDataSet(barEntriesList, "")

        barData = BarData(barDataSet)

        barChart.data = barData

        barDataSet.valueTextColor = Color.BLACK

//        barDataSet.color = Color.rgb(255, 102, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            barDataSet.color = requireContext().resources.getColor(R.color.blue, null)
        }

        barDataSet.valueTextSize = 16f

        barChart.description.isEnabled = false

        setXAxisLabels(titleList, barChart.xAxis)

        val yAxisList = ArrayList<String>()

        for(n in 0..barChart.axisLeft.axisMaximum.toInt()){
            yAxisList.add("$n")
        }

        setYAxisLabels(yAxisList, barChart.axisLeft)

        barChart.animateY(3000 , Easing.EaseInBack )

    }

    private fun setIncidentChart(incident: RentFeesResponse.Data.RequestData.Incident?) {

        val barEntriesList =  ArrayList<BarEntry>()

//        data?.request_data.maintenance!!.forEachIndexed{index, data ->
//            barEntriesList.add(BarEntry(index.toFloat(), data.toFloat()))
//        }

        barEntriesList.add(BarEntry(1f, incident?.resolved?.toFloat()!!))
        barEntriesList.add( BarEntry(2f, incident.under_review.toFloat()))

        barChart = binding.idIncidentChart

        barChart.axisRight.isEnabled = false

        barChart.setDrawBarShadow(false)

        barChart.setDrawValueAboveBar(true)

        barDataSet = BarDataSet(barEntriesList, "")

        barData = BarData(barDataSet)

        barChart.data = barData

        barDataSet.valueTextColor = Color.BLACK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            barDataSet.color = requireContext().resources.getColor(R.color.blue, null)
        }

        barDataSet.valueTextSize = 16f

        barChart.description.isEnabled = false

        val titleList = ArrayList<String>()
        titleList.add( "Under Review")
        titleList.add( "Resolved")

        setXAxisLabels(titleList, barChart.xAxis)

        val yAxisList = ArrayList<String>()

        for(n in 0..barChart.axisLeft.axisMaximum.toInt()){
            yAxisList.add("$n")
        }

        setYAxisLabels(yAxisList, barChart.axisLeft)

        barChart.animateY(4000 , Easing.EaseInBack )

    }

    private fun setMaintenanceChart(data: RentFeesResponse.Data.RequestData.Maintenance?) {

        val barEntriesList =  ArrayList<BarEntry>()

//        data?.request_data.maintenance!!.forEachIndexed{index, data ->
//            barEntriesList.add(BarEntry(index.toFloat(), data.toFloat()))
//        }

        barEntriesList.add(BarEntry(1f, data?.complete?.toFloat()!!))
        barEntriesList.add(BarEntry(2f, data.inprogress.toFloat()))

        barChart = binding.idMaintenanceChart

        barChart.axisRight.isEnabled = false

        barChart.setDrawBarShadow(false)

        barChart.setDrawValueAboveBar(true)

        barDataSet = BarDataSet(barEntriesList, "")

        barData = BarData(barDataSet)

        barChart.data = barData

        barDataSet.valueTextColor = Color.BLACK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            barDataSet.color = requireContext().resources.getColor(R.color.blue, null)
        }

        barDataSet.valueTextSize = 16f

        barChart.description.isEnabled = false

        val titleList = ArrayList<String>()
        titleList.add("In Progress")
        titleList.add("Completed")

        setXAxisLabels(titleList, barChart.xAxis)

        val yAxisList = ArrayList<String>()

        for(n in 0..barChart.axisLeft.axisMaximum.toInt()){
            yAxisList.add("$n")
        }

        setYAxisLabels(yAxisList, barChart.axisLeft)

        barChart.animateY(3000 , Easing.EaseInBack )

    }

    private fun setBillingChart(data: RentFeesResponse.Data?) {

        val barEntriesList =  ArrayList<BarEntry>()

        data?.billing_amount!!.forEachIndexed{index, data ->
            barEntriesList.add(BarEntry(index.toFloat(), data.toFloat()))
        }

        barChart = binding.idBillingChart

        barChart.axisRight.isEnabled = false

        barChart.setDrawBarShadow(false)

        barChart.setDrawValueAboveBar(true)

        barDataSet = BarDataSet(barEntriesList, "")

        barData = BarData(barDataSet)

        barChart.data = barData

        barDataSet.valueTextColor = Color.BLACK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            barDataSet.color = requireContext().resources.getColor(R.color.blue, null)
        }

        barDataSet.valueTextSize = 16f

        barChart.description.isEnabled = false

        setXAxisLabels(data.billing_title, barChart.xAxis)

        val yAxisList = ArrayList<String>()

        for(n in 0..barChart.axisLeft.axisMaximum.toInt()){
            yAxisList.add("$$n")
        }

        setYAxisLabels(yAxisList, barChart.axisLeft)

        barChart.animateY(4000 , Easing.EaseInBack )

    }

    private fun setXAxisLabels(xAxisLabels: List<String>, xAxis: XAxis) {

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            xAxis.textColor = requireContext().resources.getColor(R.color.blueDark, null)
        }
        xAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_regular)
        xAxis.textSize = 10.0f
        xAxis.setCenterAxisLabels(false)
//        xAxis.setAvoidFirstLastClipping(true)
        xAxis.setDrawGridLines(false)
        xAxis.labelCount = xAxisLabels.size
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)

    }

    private fun setYAxisLabels(yAxisLabels: List<String>, yAxis: YAxis) {

        yAxis.isGranularityEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            yAxis.textColor = resources.getColor(R.color.blueDark, null)
        }
        yAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_light)
        yAxis.textSize = 8.0f
        yAxis.setCenterAxisLabels(false)
        yAxis.setDrawGridLines(false)
        yAxis.labelCount = yAxisLabels.size
        yAxis.valueFormatter = IndexAxisValueFormatter(yAxisLabels)

    }

    private fun initClickListeners() {

        binding.btnSubscribe.setOnClickListener {
            findNavController().navigate(R.id.action_managementDashboardFragment_to_plansFragment2)
        }

//        binding.tvStartDate.setOnClickListener {
//            setStartDate()
//        }
//
//        binding.tvEndDate.setOnClickListener {
//            setEndDate()
//        }

    }

//    @SuppressLint("SetTextI18n")
//    private fun setEndDate() {
//
//        val constraintsBuilder =
//            CalendarConstraints.Builder()
//                .setValidator(DateValidatorPointBackward.now())
//        val datePicker =
//            MaterialDatePicker.Builder.datePicker()
//                .setTheme(R.style.MaterialCalendarTheme)
//                .setCalendarConstraints(constraintsBuilder.build())
//                .setTitleText("Select Date")
//                .build()
//
//        datePicker.addOnPositiveButtonClickListener {
//            binding.tvEndDate.text = DateUtils.getDateStringFromMillis(it)
//
//        }
//
//        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun setStartDate() {
//        val constraintsBuilder =
//            CalendarConstraints.Builder()
//                .setValidator(DateValidatorPointBackward.now())
//        val datePicker =
//            MaterialDatePicker.Builder.datePicker()
//                .setTheme(R.style.MaterialCalendarTheme)
//                .setCalendarConstraints(constraintsBuilder.build())
//                .setTitleText("Select Date")
//                .build()
//
//        datePicker.addOnPositiveButtonClickListener {
//            binding.tvStartDate.text = DateUtils.getDateStringFromMillis(it)
//
//        }
//
//        datePicker.show(childFragmentManager, AppConsts.DATE_PICKER_TAG)
//    }

}