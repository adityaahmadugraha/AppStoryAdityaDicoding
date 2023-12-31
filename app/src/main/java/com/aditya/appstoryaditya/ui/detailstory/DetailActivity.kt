package com.aditya.appstoryaditya.ui.detailstory


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aditya.appstoryaditya.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Back"


        getDetail()
    }

    private fun getDetail() {

        val description = intent.getStringExtra("description").toString()
        val name = intent.getStringExtra("name").toString()
        val image = intent.getStringExtra("image").toString()
        val tvDeskripsi = binding.tvDescription
        tvDeskripsi.text = description

        val tvName = binding.tvTitle
        tvName.text = name

        Glide.with(this)
            .load(image)
            .into(binding.imgStory)

//        binding.apply {
//            tvTitle.text = story?.name
//            tvDescription.text = story?.description
//            Glide.with(this@DetailActivity)
//                .load(story?.photoUrl)
//                .placeholder(this@DetailActivity.createProgress())
//                .error(android.R.color.darker_gray)
//                .into(imgStory)
//        }
    }
}