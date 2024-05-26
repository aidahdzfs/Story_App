package com.bangkit.storyapp.view.main.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.storyapp.data.api.response.ListStoryItem
import com.bangkit.storyapp.databinding.StoryItemBinding
import com.bangkit.storyapp.view.detail.DetailActivity
import com.bumptech.glide.Glide

class StoryAdapter(private val listStory: List<ListStoryItem>): RecyclerView.Adapter<StoryAdapter.ViewHolder>(){
    class ViewHolder (var binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = listStory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStory[position]

        with(holder.binding) {
            Glide.with(root.context)
                .load(story.photoUrl)
                .into(imgStory)
            userName.text= story.name
            descStory.text = story.description
            root.setOnClickListener {
                val intent = Intent(root.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        root.context as Activity,
                        Pair(imgStory, "img"),
                        Pair(userName, "name"),
                        Pair(descStory, "desc")
                    )
                root.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}