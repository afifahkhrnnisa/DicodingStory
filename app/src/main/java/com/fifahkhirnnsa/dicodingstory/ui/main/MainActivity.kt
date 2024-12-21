package com.fifahkhirnnsa.dicodingstory.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.fifahkhirnnsa.dicodingstory.R
import com.fifahkhirnnsa.dicodingstory.databinding.ActivityMainBinding
import com.fifahkhirnnsa.dicodingstory.ui.detail.DetailActivity
import com.fifahkhirnnsa.dicodingstory.ui.maps.MapsActivity
import com.fifahkhirnnsa.dicodingstory.ui.register.ViewModelFactory
import com.fifahkhirnnsa.dicodingstory.ui.upload.UploadActivity
import com.fifahkhirnnsa.dicodingstory.ui.welcome.WelcomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private var shouldScrollToTop = false
    private var isFirstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                Log.d("MainActivity", "Token from session: ${user.token}")
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupAction()
        setupRecyclerView()

        lifecycleScope.launch {
            viewModel.stories.observe(this@MainActivity) { pagingData ->
                storyAdapter.submitData(lifecycle, pagingData)
            }
        }

        lifecycleScope.launch {
            storyAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is LoadState.NotLoading -> {
                        binding.progressBar.visibility = View.GONE

                        if (shouldScrollToTop || isFirstLoad) {
                            binding.rvMain.scrollToPosition(0)
                            shouldScrollToTop = false
                            isFirstLoad = false
                        }
                    }

                    is LoadState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to load stories. Please check your connection.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                if (loadStates.append is LoadState.NotLoading) {
                    val firstVisiblePosition =
                        (binding.rvMain.layoutManager as LinearLayoutManager)
                            .findFirstVisibleItemPosition()
                    if (firstVisiblePosition == 0) {
                        binding.rvMain.scrollToPosition(0)
                    }
                }
            }
        }


        binding.addStoryButton.setOnClickListener {
            shouldScrollToTop = true
            @Suppress("DEPRECATION")
            startActivityForResult(Intent(this, UploadActivity::class.java), REQUEST_CODE_ADD_STORY)
        }

        binding.actionMaps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (!storyAdapter.snapshot().isEmpty() && shouldScrollToTop) {
            storyAdapter.refresh()
        }
    }

    private fun setupAction() {
        binding.actionLogout.setOnClickListener {
            MaterialAlertDialogBuilder(this).apply {
                setTitle("Confirm Logout")
                setMessage(getString(R.string.logout_text))
                setCancelable(false)
                setPositiveButton("Yes") { _, _ -> viewModel.logout() }
                setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                create()
                show()
            }
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter { story ->
            shouldScrollToTop = false
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("storyDetail", story)
            }
            startActivity(intent)
        }

        binding.rvMain.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_STORY = 1
    }
}