package com.fifahkhirnnsa.dicodingstory.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fifahkhirnnsa.dicodingstory.databinding.ItemStoryBinding
import androidx.core.util.Pair
import com.fifahkhirnnsa.dicodingstory.data.database.ListItemStory
import com.fifahkhirnnsa.dicodingstory.ui.detail.DetailActivity

class StoryAdapter(
    private val onItemClick: (ListItemStory) -> Unit
) : PagingDataAdapter<ListItemStory, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListItemStory) {
            binding.apply {
                tvItemName.text = story.name
                tvItemDesc.text = story.description
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(ivItemPhoto)

                root.setOnClickListener {
                    onItemClick(story)
                }
            }

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.ivItemPhoto, "image"),
                    Pair(binding.tvItemName, "name"),
                    Pair(binding.tvItemDesc, "description")
                )

                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("storyDetail", story)
                }

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItemStory>() {
            override fun areItemsTheSame(oldItem: ListItemStory, newItem: ListItemStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListItemStory, newItem: ListItemStory): Boolean {
                return oldItem == newItem
            }
        }
    }
}
