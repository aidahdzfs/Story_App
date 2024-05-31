package com.bangkit.storyapp.view.main

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.storyapp.R
import com.bangkit.storyapp.databinding.ActivityMainBinding
import com.bangkit.storyapp.view.ViewModelFactory
import com.bangkit.storyapp.view.main.adapter.StoryAdapter
import com.bangkit.storyapp.view.maps.MapsActivity
import com.bangkit.storyapp.view.newstory.NewStoryActivity
import com.bangkit.storyapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var adapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = StoryAdapter()
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recycleView.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.recycleView.layoutManager = LinearLayoutManager(this)
        }

        setAppBar()
        showLoading(false)
        setView()
    }

    private fun setView(){
        binding.fabNewStory.setOnClickListener {
            val intent = Intent(this@MainActivity, NewStoryActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin){
                startActivity(Intent(this, WelcomeActivity::class.java))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                finish()
            } else {
                mainViewModel.isLoading.observe(this){
                    showLoading(it)
                }
                setStory()
            }
        }
    }
    private fun setAppBar(){
        binding.topAppBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.logout -> {
                    lifecycleScope.launch {
                        mainViewModel.logout()
                        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        finish()
                    }
                    true
                }
                R.id.maps -> {
                    startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setStory(){
        binding.recycleView.adapter = adapter
        mainViewModel.getStory.observe(this@MainActivity){
            adapter.submitData(lifecycle, it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (!adapter.snapshot().isEmpty()){
            adapter.refresh()
            lifecycleScope.launch {
                adapter.loadStateFlow
                    .collect{
                        binding.recycleView.smoothScrollToPosition(0)
                    }
            }
        }
    }
}