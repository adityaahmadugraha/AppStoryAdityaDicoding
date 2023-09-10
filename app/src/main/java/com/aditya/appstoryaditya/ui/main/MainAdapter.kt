package com.aditya.appstoryaditya.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aditya.appstoryaditya.util.Constant.createProgress
import com.aditya.appstoryaditya.R
import com.aditya.appstoryaditya.databinding.ItemStoriesBinding
import com.aditya.appstoryaditya.models.Story
import com.aditya.appstoryaditya.ui.detailstory.DetailActivity
import com.aditya.appstoryaditya.util.Constant.KEY_STORY
import com.bumptech.glide.Glide
import java.util.Random

class MainAdapter :
    PagingDataAdapter<Story, MainAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            holder.bind(data)
        }
    }

    inner class ViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            binding.apply {
                val context = itemView.context
                val firstLetter = data.name.first().toString()
                val date = itemView.context.getString(
                    R.string.created_at,
                    data.createdAt?.split("T")?.get(0) ?: ""
                )

                tvNama.text = data.name
                tvCreatedAt.text = date
                tvFirstLetter.text = firstLetter

                val rnd = Random()
                val color: Int =
                    Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                cardView.backgroundTintList = ColorStateList.valueOf(color)

                Glide.with(context)
                    .load(data.photoUrl)
                    .placeholder(itemView.context.createProgress())
                    .error(android.R.color.darker_gray)
                    .into(imgStories)

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(KEY_STORY, data)
                    context.startActivity(intent)
                }

            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Story> =
            object : DiffUtil.ItemCallback<Story>() {
                override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                    return oldItem == newItem
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    }
}