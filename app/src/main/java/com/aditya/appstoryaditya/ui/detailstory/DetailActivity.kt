package com.aditya.appstoryaditya.ui.detailstory


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aditya.appstoryaditya.databinding.ActivityDetailBinding
import com.aditya.appstoryaditya.ui.main.MainActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        getDetail()

        binding.icBack.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDetail() {

        val description = intent.getStringExtra("description").toString()
        val name = intent.getStringExtra("name").toString()
        val image = intent.getStringExtra("image").toString()
        val tanggal = intent.getStringExtra("tanggal").toString()


        val tvDeskripsi = binding.tvDescription
        tvDeskripsi.text = description

        val tvName = binding.tvTitle
        tvName.text = name

        val tvTanggal = binding.tvTngal
        tvTanggal.text = tanggal

        Glide.with(this)
            .load(image)
            .into(binding.imgStory)

    }
}