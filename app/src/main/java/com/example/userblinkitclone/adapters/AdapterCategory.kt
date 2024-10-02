package com.example.userblinkitclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userblinkitclone.databinding.ItemViewProductCategoryBinding
import com.example.userblinkitclone.models.Category

class AdapterCategory(
    private val categoryList: ArrayList<Category>,
    private val onCategoryIconClicked: (Category) -> Unit) :
    RecyclerView.Adapter<AdapterCategory.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding: ItemViewProductCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemViewProductCategoryBinding.inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding.apply {
            ivCategoryImage.setImageResource(category.image)
            tvCategoryTitle.text = category.title
        }
        holder.itemView.setOnClickListener {
            onCategoryIconClicked(category)
        }
    }
}