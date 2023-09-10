package com.aditya.appstoryaditya.ui.detailstory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aditya.appstoryaditya.R
import com.aditya.appstoryaditya.databinding.ActivityDetailBinding
import com.aditya.appstoryaditya.models.Story
import com.aditya.appstoryaditya.util.Constant.KEY_STORY
import com.aditya.appstoryaditya.util.Constant.createProgress
import com.bumptech.glide.Glide


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var story: Story? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        story = intent.getParcelableExtra<Story>(KEY_STORY) as Story

        setView()
        supportActionBar?.title = getString(R.string.detail_story)

    }

    private fun setView() {
        binding.apply {
            tvNama.text = story?.name
            tvDescription.text = story?.description
            Glide.with(this@DetailActivity)
                .load(story?.photoUrl)
                .placeholder(this@DetailActivity.createProgress())
                .error(android.R.color.darker_gray)
                .into(imgStory)
        }
    }
}