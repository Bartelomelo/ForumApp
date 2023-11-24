package com.example.froumapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.froumapp.data.responses.Forum
import com.example.froumapp.databinding.ForumListBinding


class ForumListAdapter(private val onItemClicked: (Forum) -> Unit) :
    ListAdapter<Forum, ForumListAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ForumListBinding.inflate(LayoutInflater.from(parent.context))
        )


    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ViewHolder(private var binding: ForumListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Forum) {
            binding.apply {
                forumName.text = item.name
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Forum>() {
            override fun areItemsTheSame(oldItem: Forum, newItem: Forum): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Forum, newItem: Forum): Boolean {
                return oldItem._id == newItem._id            }
        }
    }
}