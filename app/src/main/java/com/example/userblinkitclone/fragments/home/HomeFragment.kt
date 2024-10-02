package com.example.userblinkitclone.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userblinkitclone.R
import com.example.userblinkitclone.adapters.AdapterCategory
import com.example.userblinkitclone.databinding.FragmentHomeBinding
import com.example.userblinkitclone.models.Category
import com.example.userblinkitclone.util.Constants

class HomeFragment : Fragment() {
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
        setAllCategories()
        navigateToSearchFragment()
    }

    private fun navigateToSearchFragment() {
        binding.searchEdit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun setAllCategories() {
        binding.rvCategory.adapter = AdapterCategory(Constants.getCategoryList)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}