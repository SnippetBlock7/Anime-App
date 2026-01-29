package com.example.assignment.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.databinding.ListCardViewBinding
import com.example.assignment.model.db.AnimeEntity
import com.example.assignment.util.loadImage

// Inheriting from ListAdapter handles the list and DiffUtil automatically
class AnimeListAdapter(private val onItemClicked: (AnimeEntity) -> Unit) :
    ListAdapter<AnimeEntity, AnimeListAdapter.ViewHolder>(AnimeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }

    class ViewHolder(private val binding: ListCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AnimeEntity, onItemClicked: (AnimeEntity) -> Unit) {
            binding.animeList = item

            loadImage(binding.poster, item.imageUrl)

            binding.root.setOnClickListener {
                onItemClicked(item)
            }
            binding.executePendingBindings()
        }
    }

    class AnimeDiffCallback : DiffUtil.ItemCallback<AnimeEntity>() {
        override fun areItemsTheSame(oldItem: AnimeEntity, newItem: AnimeEntity): Boolean {
            // IDs are the most reliable way to check if it's the same item
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AnimeEntity, newItem: AnimeEntity): Boolean {
            // Compares all fields in the data class
            return oldItem == newItem
        }
    }
}