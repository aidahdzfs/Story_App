package com.bangkit.storyapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.storyapp.data.api.response.ListStoryItem
import com.bangkit.storyapp.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent.getParcelableExtra<ListStoryItem>(EXTRA_USER) as ListStoryItem
        setDetail(intent)


    }

    private fun setDetail(details: ListStoryItem){
        with(binding){
            Glide.with(this@DetailActivity)
                .load(details.photoUrl)
                .into(imgStory)
            tvUser.text = details.name
            tvDesc.text = details.description
        }
    }
    companion object {
        const val EXTRA_USER = "extra_user"
    }
}