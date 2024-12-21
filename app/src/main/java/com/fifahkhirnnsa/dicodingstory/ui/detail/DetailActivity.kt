package com.fifahkhirnnsa.dicodingstory.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fifahkhirnnsa.dicodingstory.data.database.ListItemStory
import com.fifahkhirnnsa.dicodingstory.databinding.ActivityDetailBinding
import com.fifahkhirnnsa.dicodingstory.ui.register.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val storyDetail: ListItemStory? = intent.getParcelableExtra("storyDetail")
            viewModel.setStoryDetail(storyDetail)
        }

        viewModel.storyDetail.observe(this) { story ->
            if (story != null) {
                displayStoryDetail(story)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun displayStoryDetail(story: ListItemStory) {
        binding.tvDetailName.text = story.name
        binding.tvDetailDescription.text = story.description
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.ivDetailPhoto)
    }
}
