package com.example.userblinkitclone.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.userblinkitclone.R
import com.example.userblinkitclone.activities.CartListener
import com.example.userblinkitclone.adapters.ProductAdapter
import com.example.userblinkitclone.databinding.FragmentCategoryBinding
import com.example.userblinkitclone.databinding.ItemViewProductBinding
import com.example.userblinkitclone.models.Product
import com.example.userblinkitclone.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding: FragmentCategoryBinding get() =  _binding!!
    private val viewModel: UserViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private var cartListener: CartListener? = null
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
                productAdapter = ProductAdapter(::onAddButtonClicked, ::onIncrementButtonClicked, ::onDecrementButtonClicked)
                binding.rvProducts.adapter = productAdapter
                productAdapter.differ.submitList(products)
                binding.shimmerViewContainer.visibility = View.GONE
            }
        }
    }

    private fun onAddButtonClicked(product: Product, productBinding: ItemViewProductBinding) {
        productBinding.tvAdd.visibility = View.GONE
        productBinding.llProductCount.visibility = View.VISIBLE

        var itemCount = productBinding.tvProductCount.text.toString().toInt()
        itemCount++
        productBinding.tvProductCount.text = itemCount.toString()

        cartListener?.showCartLayout(1)
    }

    private fun onIncrementButtonClicked(product: Product, productBinding: ItemViewProductBinding) {

        var itemCountIncrement = productBinding.tvProductCount.text.toString().toInt()
        itemCountIncrement++
        productBinding.tvProductCount.text = itemCountIncrement.toString()

        cartListener?.showCartLayout(1)
    }

    private fun onDecrementButtonClicked(product: Product, productBinding: ItemViewProductBinding) {
        var itemCountDecrement = productBinding.tvProductCount.text.toString().toInt()
        itemCountDecrement--

        if (itemCountDecrement > 0) {
            productBinding.tvProductCount.text = itemCountDecrement.toString()
        } else {
            productBinding.tvAdd.visibility = View.VISIBLE
            productBinding.llProductCount.visibility = View.GONE
            productBinding.tvProductCount.text = "0"
        }
        cartListener?.showCartLayout(-1)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CartListener) cartListener = context
        else throw ClassCastException("Please implement cart listener")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}