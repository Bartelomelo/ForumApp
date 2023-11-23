package com.example.froumapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.froumapp.data.responses.CategoryResponseItem
import com.example.froumapp.databinding.CategoryListBinding


class CategoryListAdapter(private val onItemClicked: (CategoryResponseItem) -> Unit) :
    ListAdapter<CategoryResponseItem, CategoryListAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            CategoryListBinding.inflate(LayoutInflater.from(parent.context))
        )


    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ViewHolder(private var binding: CategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryResponseItem) {
            binding.apply {
                categoryName.text = item.title
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CategoryResponseItem>() {
            override fun areItemsTheSame(oldItem: CategoryResponseItem, newItem: CategoryResponseItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: CategoryResponseItem, newItem: CategoryResponseItem): Boolean {
                return oldItem._id == newItem._id            }
        }
    }
}