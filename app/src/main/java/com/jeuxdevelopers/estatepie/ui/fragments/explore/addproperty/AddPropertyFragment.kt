package com.jeuxdevelopers.estatepie.ui.fragments.explore.addproperty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.databinding.FragmentAddPropertyBinding
import com.jeuxdevelopers.estatepie.ui.dialogs.property.PropertyTypeDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.SelectCategoryDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPropertyFragment : Fragment() {


    private lateinit var binding: FragmentAddPropertyBinding
    private lateinit var viewModel: AddPropertyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }
        binding.tvSelectCategory.setOnClickListener{
            activity?.let { it1 -> SelectCategoryDialog().show(it1.supportFragmentManager, "MyCustomFragment") }
        }
        binding.tvPropertyType.setOnClickListener{
            activity?.let { it1 -> PropertyTypeDialog{
                //TODO: set data to input field
            }.show(it1.supportFragmentManager, "MyCustomFragment") }
        }
    }

}