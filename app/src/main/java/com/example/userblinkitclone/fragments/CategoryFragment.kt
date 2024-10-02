package com.example.userblinkitclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.userblinkitclone.R
import com.example.userblinkitclone.adapters.ProductAdapter
import com.example.userblinkitclone.databinding.FragmentCategoryBinding
import com.example.userblinkitclone.models.Product
import com.example.userblinkitclone.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding: FragmentCategoryBinding get() =  _binding!!
    private val viewModel: UserViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private val categoryTitle by lazy { arguments?.getString("category") ?: ""}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.toolbar.title = categoryTitle
        fetchCategoryProduct()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_categoryFragment_to_homeFragment)
        }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.search_menu) {
                findNavController().navigate(R.id.action_categoryFragment_to_searchFragment)
                true
            } else false
        }
    }

    private fun fetchCategoryProduct() {
        if (_binding == null) return

        binding.shimmerViewContainer.visibility = View.VISIBLE

        // Use lifecycleScope with viewLifecycleOwner to respect fragment lifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getCategoryProduct(categoryTitle).collect { products ->
                // Check if binding is still initialized before using it
                if (_binding == null) return@collect

                if (products.isEmpty()) {
                    binding.rvProducts.visibility = View.GONE
                    binding.tvNoProductAdded.visibility = View.VISIBLE
                } else {
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvNoProductAdded.visibility = View.GONE
                }
                productAdapter = ProductAdapter()
                binding.rvProducts.adapter = productAdapter
                productAdapter.differ.submitList(products)
                binding.shimmerViewContainer.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}