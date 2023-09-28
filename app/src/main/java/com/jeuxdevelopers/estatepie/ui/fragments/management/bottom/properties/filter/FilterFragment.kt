package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentFilterBinding
import com.jeuxdevelopers.estatepie.models.shared.SharedViewModel
import com.jeuxdevelopers.estatepie.utils.AppConsts
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterBinding

    private val viewModel: FilterViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)

        initAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initAdapter() {

        val azAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter, R.id.adapterTxtView, resources.getStringArray(R.array.asec_desc)
        )
        binding.etAllProperties.setAdapter(azAdapter)

        binding.etAllProperties.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                when(arguments?.getInt(AppConsts.INTENT_ID)){
                    1 -> {

                        if (position == 0){
                            sharedViewModel.sortResidential("asc")
                            findNavController().navigateUp()
                        }else{
                            sharedViewModel.sortResidential("desc")
                            findNavController().navigateUp()
                        }

                    } 2 -> {

                        if (position == 0){
                            sharedViewModel.sortCommercial("asc")
                            findNavController().navigateUp()
                        }else{
                            sharedViewModel.sortCommercial("desc")
                            findNavController().navigateUp()
                        }

                    } 3 -> {

                        if (position == 0){
                            sharedViewModel.sortAds("asc")
                            findNavController().navigateUp()
                        }else{
                            sharedViewModel.sortAds("desc")
                            findNavController().navigateUp()
                        }

                    }

                }
            }


    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{

            findNavController().navigateUp()

        }

    }

}