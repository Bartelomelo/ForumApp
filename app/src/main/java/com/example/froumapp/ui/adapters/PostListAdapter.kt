package com.example.froumapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.froumapp.data.responses.Post
import com.example.froumapp.databinding.PostListBinding
import com.example.froumapp.ui.forum.thread.ThreadViewModel
import com.squareup.picasso.Picasso


class PostListAdapter(
    private val token: String,
    private val userId: String,
    private val viewModel: ThreadViewModel,
    private val onItemClicked: (Post) -> Unit
) :
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
        val profilePhoto = holder.itemView.findViewById<ImageView>(com.example.froumapp.R.id.user_profile_image_post)
        Picasso.with(holder.itemView.context)
            .load("http://10.0.2.2:5000/public/images/users/${current.author.username}/${current.author.profilePicture}")
            .error(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    com.example.froumapp.R.drawable.empty_profile_picture
                )
            )
            .into(profilePhoto)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    inner class ViewHolder(private var binding: PostListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post) {
            binding.apply {
                if (item.author._id == userId) {
                    upvote.visibility = View.INVISIBLE
                    downvote.visibility = View.GONE
                }
                username.text = item.author.username
                postDescription.text = item.comment
                votes.text = (item.upvotes.size - item.downvotes.size).toString()
                upvote.setOnClickListener {
                    if (!item.upvotes.contains(userId) && item.author._id != userId) {
                        viewModel.votePost("Berear $token", item._id, "1", userId)
                        votes.text = (votes.text.toString().toInt() + 1).toString()
                    }
                }
                downvote.setOnClickListener {
                    if (!item.downvotes.contains(userId) && item.author._id != userId) {
                        viewModel.votePost("Berear $token", item._id, "0", userId)
                        votes.text = (votes.text.toString().toInt() - 1).toString()
                    }
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem._id == newItem._id
            }
        }
    }
}