package com.example.storyfirst.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.storyfirst.databinding.ItemStoriesBinding
import com.example.storyfirst.model.GetStoriesResponse

class StoriesAdapter (private val datas: ArrayList<GetStoriesResponse.ListStoryItem>,
val ctx: Context
) :
RecyclerView.Adapter<StoriesAdapter.MyViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class MyViewHolder(val binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: GetStoriesResponse.ListStoryItem) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(stories)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(stories.photoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivImage)
                tvDescription.text = stories.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder((itemView))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    override fun getItemCount(): Int = datas.size

    fun setData(data: ArrayList<GetStoriesResponse.ListStoryItem>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: GetStoriesResponse.ListStoryItem)
    }
}