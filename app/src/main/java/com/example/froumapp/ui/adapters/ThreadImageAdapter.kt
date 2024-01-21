package com.example.froumapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.froumapp.R
import com.example.froumapp.databinding.ImageListBinding
import com.squareup.picasso.Picasso

class ThreadImageAdapter(private val threadId: String, private val onItemClicked: () -> Unit) :
    ListAdapter<String, ThreadImageAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ImageListBinding.inflate(LayoutInflater.from(parent.context))
        )


    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        val threadImage = holder.itemView.findViewById<ImageView>(R.id.thread_image)
        Picasso.with(holder.itemView.context).load("http://10.0.2.2:5000/public/images/threads/${threadId}/${current}")
            .into(threadImage)
        holder.itemView.setOnClickListener {
            onItemClicked()
        }
        holder.bind(current)
    }

    class ViewHolder(private var binding: ImageListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem           }
        }
    }
}