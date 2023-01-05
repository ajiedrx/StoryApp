package com.dicoding.storyapp.presentation.story.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dicoding.storyapp.base.BaseActivity
import com.dicoding.storyapp.base.BaseResult
import com.dicoding.storyapp.databinding.ActivityStoryDetailBinding
import com.dicoding.storyapp.domain.story.Story
import com.dicoding.storyapp.presentation.story.StoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryDetailActivity : BaseActivity() {

    companion object{
        fun start(context: Context, storyId: String) {
            val starter = Intent(context, StoryDetailActivity::class.java)
            starter.putExtra(STORY_ID, storyId)
            context.startActivity(starter)
        }

        const val STORY_ID = "storyId"
    }

    private var _binding: ActivityStoryDetailBinding? = null
    private val binding get() = _binding!!


    private val storyViewModel: StoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()
        val storyId: String = intent.getStringExtra(STORY_ID).orEmpty()
        storyViewModel.getStoryDetail(storyId)
    }

    private fun initObserver(){
        storyViewModel.getStoryDetail.observe(this){
            when (it) {
                is BaseResult.Loading -> {
                    showProgressDialog()
                }
                is BaseResult.Success -> {
                    hideProgressDialog()
                    bindToUI(it.data)
                    playAnimation()
                }
                is BaseResult.Error -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                }
                else -> {
                    hideProgressDialog()
                }
            }
        }
    }

    private fun playAnimation(){
        val name = ObjectAnimator.ofFloat(binding.tvDetailName, View.ALPHA, 1f).setDuration(500)
        val description = ObjectAnimator.ofFloat(binding.tvDetailDescription, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(name, description)
            start()
        }
    }

    private fun bindToUI(data: Story) {
        Glide
            .with(this)
            .load(data.photoUrl)
            .centerCrop()
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = data.name
        binding.tvDetailDescription.text = data.description
    }
}