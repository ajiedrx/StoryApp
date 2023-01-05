package com.dicoding.storyapp.presentation.story.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.RvStoryItemBinding
import com.dicoding.storyapp.domain.story.Story
import com.dicoding.storyapp.presentation.story.detail.StoryDetailActivity

class StoryListAdapter: Adapter<StoryListAdapter.ItemViewHolder>() {

    private val callback : DiffUtil.ItemCallback<Story> = object: DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Story, newItem: Story) =
            oldItem == newItem
    }

    private val differList = AsyncListDiffer(this, this.callback).also {
        it.submitList(mutableListOf())
    }

    private var _binding: RvStoryItemBinding? = null
    private val binding get() = _binding!!

    class ItemViewHolder(binding: RvStoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        _binding = RvStoryItemBinding.inflate(LayoutInflater.from(parent.context))
//        binding.root.layoutParams = (
//                RecyclerView.LayoutParams(
//                    RecyclerView.LayoutParams.MATCH_PARENT,
//                    RecyclerView.LayoutParams.WRAP_CONTENT
//                )
//        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        with(binding){
            with(differList.currentList[position]){
                tvItemName.text = name
                tvItemDescription.text = description
                Glide
                    .with(holder.itemView.context)
                    .load(photoUrl)
                    .centerCrop()
                    .into(ivItemPhoto)

                holder.itemView.setOnClickListener {
                    StoryDetailActivity.start(holder.itemView.context, id)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    fun setData(data: List<Story>) {
        differList.submitList(data)
    }
}