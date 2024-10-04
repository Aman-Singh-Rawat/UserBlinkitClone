package com.example.userblinkitclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.userblinkitclone.databinding.ItemViewProductBinding
import com.example.userblinkitclone.models.Product
import com.example.userblinkitclone.util.FilteringProducts

class ProductAdapter(
    private val onAddButtonClicked: (Product, ItemViewProductBinding) -> Unit,
    private val onIncrementButtonClicked: (Product, ItemViewProductBinding) -> Unit,
    private val onDecrementButtonClicked: (Product, ItemViewProductBinding) -> Unit
)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(), Filterable {

    inner class ProductViewHolder(val binding: ItemViewProductBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.productRandomId == newItem.productRandomId

        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(ItemViewProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()
            val productImage = product.productImageUris

            productImage?.let {
                it.forEachIndexed { index, s ->
                    if (s != null) imageList.add(SlideModel(s))
                }
            }

            ivImageSlider.setImageList(imageList)
            tvProductTitle.text = product.productTitle
            tvProductQuantity.text = "${product.productQuantity}${product.productUnit}"

            tvProductPrice.text = "₹${product.productPrice}"

            tvAdd.setOnClickListener { onAddButtonClicked(product, this) }
            tvIncrement.setOnClickListener { onIncrementButtonClicked(product, this) }
            tvProductDecrement.setOnClickListener { onDecrementButtonClicked(product, this) }
        }
    }


    private val filter: FilteringProducts? = null
    var originalList = ArrayList<Product>()

    override fun getFilter(): Filter {
        if (filter == null) return FilteringProducts(this, originalList)
        return filter
    }
}