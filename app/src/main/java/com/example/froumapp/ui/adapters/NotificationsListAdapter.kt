package com.example.froumapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.froumapp.data.responses.NotificationResponseItem
import com.example.froumapp.databinding.NotificationListBinding


class NotificationsListAdapter(private val onItemClicked: (NotificationResponseItem) -> Unit) :
    ListAdapter<NotificationResponseItem, NotificationsListAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            NotificationListBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ViewHolder(private var binding: NotificationListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationResponseItem) {
            binding.apply {
                when (item.type) {
                    "thread-author" -> notificationText.text = "Użytkownik ${item.sender.username} dodał komentarz w twoim wątku."
                    "thread" -> notificationText.text = "Użytkownik ${item.sender.username} dodał komentarz do obserwowanego wątku."
                    "forum" -> notificationText.text = "Użytkownik ${item.sender.username} dodał wątek do forum ${item.forum.name}."
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<NotificationResponseItem>() {
            override fun areItemsTheSame(
                oldItem: NotificationResponseItem,
                newItem: NotificationResponseItem
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: NotificationResponseItem,
                newItem: NotificationResponseItem
            ): Boolean {
                return oldItem._id == newItem._id
            }
        }
    }
}