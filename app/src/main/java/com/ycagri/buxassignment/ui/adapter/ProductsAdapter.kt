package com.ycagri.buxassignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ycagri.buxassignment.databinding.ItemProductBinding
import com.ycagri.buxassignment.db.ProductEntity
import com.ycagri.buxassignment.util.AppExecutors
import com.ycagri.buxassignment.util.DataBoundListAdapter

class ProductsAdapter(
    appExecutors: AppExecutors,
    private val productClickCallback: ((ProductEntity) -> Unit)?,
) :
    DataBoundListAdapter<ProductEntity, ItemProductBinding>(
        appExecutors,
        object : DiffUtil.ItemCallback<ProductEntity>() {
            override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
                return oldItem.securityId == newItem.securityId
            }

            override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
                return oldItem.displayName == newItem.displayName
            }
        }
    ) {
    override fun createBinding(parent: ViewGroup): ItemProductBinding {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        binding.root.setOnClickListener {
            binding.product?.let {
                productClickCallback?.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ItemProductBinding, item: ProductEntity) {
        binding.product = item
    }
}