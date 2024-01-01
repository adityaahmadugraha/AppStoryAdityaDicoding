package com.aditya.appstoryaditya.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditya.appstoryaditya.R
import com.aditya.appstoryaditya.databinding.ActivityMainBinding
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.ui.detailstory.DetailActivity
import com.aditya.appstoryaditya.ui.inputstory.InputStoryActivity
import com.aditya.appstoryaditya.ui.location.UserLocationActivity
import com.aditya.appstoryaditya.ui.login.LoginActivity
import com.aditya.appstoryaditya.util.Constant.tokenBearer
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val INSERT_RESULT = 200
    }

    private lateinit var binding: ActivityMainBinding
    private var user: User? = null
    private lateinit var mAdapter: MainAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getUser {
            user = it
        }
        binding.fabTambah.setOnClickListener {
            Intent(this@MainActivity, InputStoryActivity::class.java).also {
                launcherInsertStory.launch(it)
            }
        }

        setupRecyclerData()
        getStories()
        showLoading()

        supportActionBar?.title = null
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this@MainActivity) {
            binding.progressBar.isVisible = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                showLogoutConfirmationDialog()
            }

            R.id.location -> {
                Intent(this@MainActivity, UserLocationActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return true
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Konfirmasi Logout")
        builder.setMessage("Apakah Anda yakin ingin keluar?")

        builder.setPositiveButton("Ya") { _, _ ->
            viewModel.getUser {
                viewModel.logout()
                Intent(this@MainActivity, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(it)
                    finish()
                }
            }
        }

        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun reGetStory() {
        getStories()
        setupRecyclerData()
    }

    private val launcherInsertStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == INSERT_RESULT){
            reGetStory()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getStories() {
        val token = user?.tokenBearer.toString()
        viewModel.getStories(token) {
            mAdapter.submitData(lifecycle, it)
        }
    }


    private fun setupRecyclerData() {
        mAdapter = MainAdapter { story ->
            intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                putExtra("description", story.description)
                putExtra("name", story.name)
                putExtra("image", story.photoUrl)
            }
            startActivity(intent)
        }

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    mAdapter.retry()
                }
            )
        }
    }

}


