package com.example.froumapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.froumapp.R
import com.example.froumapp.data.responses.Post
import com.example.froumapp.databinding.PostListBinding
import com.squareup.picasso.Picasso

class PostListAdapter(private val onItemClicked: (Post) -> Unit) :
    ListAdapter<Post, PostListAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            PostListBinding.inflate(LayoutInflater.from(parent.context))
        )


    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        val profilePhoto = holder.itemView.findViewById<ImageView>(R.id.user_profile_image_post)
        Picasso.with(holder.itemView.context).load("http://10.0.2.2:5000/public/images/users/${current.author.username}/${current.author.profilePicture}")
            .error(ContextCompat.getDrawable(holder.itemView.context, R.drawable.empty_profile_picture))
            .into(profilePhoto)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ViewHolder(private var binding: PostListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post) {
            binding.apply {
                username.text = item.author.username
                threadDescription.text = item.comment
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem._id == newItem._id            }
        }
    }
}