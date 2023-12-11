package com.example.froumapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.froumapp.R
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.databinding.ThreadListBinding
import com.squareup.picasso.Picasso

class ThreadListAdapter(private val onItemClicked: (ThreadResponseItem) -> Unit) :
    ListAdapter<ThreadResponseItem, ThreadListAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ThreadListBinding.inflate(LayoutInflater.from(parent.context))
        )


    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        val profilePhoto = holder.itemView.findViewById<ImageView>(R.id.user_profile_image)
        Picasso.with(holder.itemView.context).load("http://10.0.2.2:5000/public/images/users/${current.author.username}/${current.author.profilePicture}")
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.empty_profile_picture))
            .into(profilePhoto)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ViewHolder(private var binding: ThreadListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ThreadResponseItem) {
            binding.apply {
                username.text = item.author.username
                title.text = item.title
                threadDescription.text = item.description
                views.text = item.views.toString()
                answers.text = item.posts?.size.toString()
                addDate.text = item.createdAt.split("T")[0]
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ThreadResponseItem>() {
            override fun areItemsTheSame(oldItem: ThreadResponseItem, newItem: ThreadResponseItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ThreadResponseItem, newItem: ThreadResponseItem): Boolean {
                return oldItem._id == newItem._id            }
        }
    }
}