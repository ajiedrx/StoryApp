package com.dicoding.storyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.RvStoryItemBinding
import com.dicoding.storyapp.domain.story.Story
import com.dicoding.storyapp.presentation.story.detail.StoryDetailActivity

class StoryListPagingAdapter :
    PagingDataAdapter<Story, StoryListPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvStoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: RvStoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            with(binding){
                with(data){
                    tvItemName.text = name
                    tvItemDescription.text = description
                    Glide
                        .with(binding.root.context)
                        .load(photoUrl)
                        .centerCrop()
                        .into(ivItemPhoto)

                    binding.root.setOnClickListener {
                        StoryDetailActivity.start(binding.root.context, id)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}