package com.example.userblinkitclone.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.userblinkitclone.R
import com.example.userblinkitclone.adapters.AdapterCategory
import com.example.userblinkitclone.databinding.FragmentHomeBinding
import com.example.userblinkitclone.models.Category
import com.example.userblinkitclone.util.Constants
import com.example.userblinkitclone.viewmodels.UserViewModel

class HomeFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels ()
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        getCartData()
        setAllCategories()
        navigateToSearchFragment()
    }

    private fun navigateToSearchFragment() {
        binding.searchEdit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun getCartData() {
        viewModel.getAllCartProduct().observe(this) {
            it.forEach { Log.d("debugging", it.productTitle.toString()) }
        }
    }

    private fun setAllCategories() {
        binding.rvCategory.adapter = AdapterCategory(Constants.getCategoryList, ::onCategoryIconClicked)

    }

    private fun onCategoryIconClicked(category: Category) {
        findNavController().navigate(R.id.action_homeFragment_to_categoryFragment,
            bundleOf("category" to category.title)
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}