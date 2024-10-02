package com.example.userblinkitclone.util

import android.widget.Filter
import com.example.userblinkitclone.adapters.ProductAdapter
import com.example.userblinkitclone.models.Product
import java.util.Locale

class FilteringProducts(
    private val adapter: ProductAdapter,
    private val filter: ArrayList<Product>
) : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val result = FilterResults()
        if (!constraint.isNullOrEmpty()) {
            val filteredList = ArrayList<Product>()
            val query = constraint.toString().trim().uppercase(Locale.getDefault()).split(" ")

            filter.forEach { product ->
                if (query.any {
                        product.productTitle?.uppercase(Locale.getDefault())?.contains(it) == true ||
                        product.productCategory?.uppercase(Locale.getDefault())?.contains(it) == true ||
                        product.productPrice?.toString()?.uppercase(Locale.getDefault())?.contains(it) == true ||
                        product.productType?.uppercase(Locale.getDefault())?.contains(it) == true
                    }
                )filteredList.add(product)
            }
            result.values = filteredList
            result.count = filteredList.size
        } else {
            result.values = filter
            result.count = filter.size
        }
        return result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        adapter.differ.submitList(results?.values as ArrayList<Product>)
    }
}